API de Mesa de Ayuda (Helpdesk) con SLA — Spring Boot + JWT

API REST para gestión de tickets de soporte técnico, con autenticación JWT, refresh tokens persistidos en base de datos, autorización por rol (RBAC) y cálculo automático de SLA según prioridad.

Stack tecnológico

- Java 25 / Spring Boot 4.1.0
- Spring Web, Spring Security, Spring Data JPA
- MySQL
- Lombok, Bean Validation
- JJWT (io.jsonwebtoken) para generación y validación de JWT

## Cómo ejecutar el proyecto

### Requisitos previos

- JDK 17 o superior
- MySQL corriendo localmente (o accesible por red)
- Una base de datos creada, por ejemplo `helpdesk_db`

### 1. Variables de entorno

El proyecto no guarda credenciales ni claves sensibles en el código. Antes de correrlo, define estas variables de entorno en tu sistema:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_USERNAME` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | `tu_password` |
| `JWT_SECRET` | Clave secreta para firmar los JWT (string largo y aleatorio) | `una-clave-larga-y-aleatoria-de-al-menos-32-caracteres` |

En Windows, se configuran desde "Editar las variables de entorno del sistema" → "Variables de entorno" → "Nueva..." (en Variables de usuario). Reinicia la terminal/IDE después de crearlas.

### 2. Levantar la aplicación

```bash
./mvnw spring-boot:run
```

En Windows (PowerShell):
```powershell
.\mvnw.cmd spring-boot:run
```

Con `spring.jpa.hibernate.ddl-auto=update`, las tablas se crean automáticamente en el primer arranque a partir de las entidades — no es necesario ejecutar scripts SQL manuales.

### 3. Crear el primer usuario ADMIN

El endpoint de registro (`POST /api/auth/registro`) siempre asigna el rol `USUARIO` por diseño (nadie puede auto-asignarse un rol superior). Para tener un usuario `ADMIN` que pueda ascender a otros a `SOPORTE`, hazlo manualmente la primera vez:

```sql
UPDATE usuario SET rol = 'ADMIN' WHERE email = 'tu_correo@ejemplo.com';
```

(Registra primero ese usuario por el endpoint normal, y luego sube su rol con el `UPDATE`.)

## Estrategia de persistencia del refresh token

Se eligió la **Opción A: persistencia en base de datos**, mediante la entidad `RefreshToken` (campos: `token`, `usuario`, `expiraEn`, `revocado`).

**Por qué:**

- Permite **revocación real** en el logout. Con esta estrategia, cerrar sesión marca el token como `revocado = true` en la base de datos, y cualquier intento posterior de usarlo para renovar el access token es rechazado de inmediato (`401`). Con un refresh token stateless (Opción B), esto no es posible de forma directa: el token seguiría siendo válido hasta su expiración natural, y el logout solo podría invalidarlo del lado del cliente, no del servidor.
- Cada login genera un refresh token distinto, lo que en la práctica equivale a una sesión independiente por dispositivo/login. Esto permite, a futuro, revocar sesiones individuales sin afectar las demás del mismo usuario.
- Es la opción que el propio taller señala como más didáctica, al obligar a razonar explícitamente sobre el ciclo de vida del token (emisión, uso, expiración y revocación) en vez de delegarlo por completo a la firma criptográfica del JWT.

**Trade-off aceptado:** cada validación de refresh token implica una consulta a base de datos (a diferencia de un JWT, que se valida solo con la firma, sin tocar la BD). Para el volumen de este taller, ese costo es insignificante frente al beneficio de poder revocar sesiones de forma controlada.

## Flujo de autenticación

1. `POST /api/auth/registro` — crea un usuario con rol `USUARIO`.
2. `POST /api/auth/login` — valida credenciales (BCrypt) y devuelve `accessToken` (JWT, expira en 15 min) + `refreshToken` (UUID, expira en 7 días, persistido en BD).
3. El cliente usa el `accessToken` en el header `Authorization: Bearer <token>` para acceder a rutas protegidas.
4. Cuando el `accessToken` expira, `POST /api/auth/refresh` con el `refreshToken` devuelve un `accessToken` nuevo, sin pedir credenciales de nuevo.
5. `POST /api/auth/logout` marca el `refreshToken` como revocado — cualquier intento posterior de refrescar con ese token falla con `401`.

## Endpoints principales

| Método | Ruta | Acceso |
|---|---|---|
| GET | `/api/ping` | Público |
| POST | `/api/auth/registro` | Público |
| POST | `/api/auth/login` | Público |
| POST | `/api/auth/refresh` | Público (requiere refreshToken válido) |
| POST | `/api/auth/logout` | Autenticado |
| POST | `/api/tickets` | Autenticado |
| GET | `/api/tickets/mios` | Autenticado |
| GET | `/api/tickets/{id}` | Autenticado (dueño o SOPORTE/ADMIN) |
| GET | `/api/tickets` | SOPORTE, ADMIN |
| PATCH | `/api/tickets/{id}/estado` | SOPORTE, ADMIN |
| GET | `/api/tickets/vencidos` | SOPORTE, ADMIN |
| POST | `/api/admin/soporte` | ADMIN |

## Regla de negocio: cálculo de SLA

Al crear un ticket, el servidor calcula automáticamente `slaVenceEn` según la prioridad:

| Prioridad | SLA |
|---|---|
| ALTA | 4 horas |
| MEDIA | 24 horas |
| BAJA | 72 horas |

Un ticket se considera **vencido** cuando la fecha actual supera `slaVenceEn` y su estado no es `RESUELTO`. Este cálculo se realiza en tiempo real en cada consulta (no se persiste), para evitar datos desactualizados.
