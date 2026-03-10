# API REST - Tienda Online (MongoDB)

API REST desarrollada con Spring Boot y MongoDB para gestionar una tienda online. Proyecto de examen - Acceso a Datos.

**Autor:** Sergio Arjona

---

## Requisitos previos

- **Java 17** o superior
- **Maven** (o usar Maven Wrapper incluido)
- **MongoDB Atlas** con una base de datos configurada
- Variable de entorno **`MONGODB_URI`** con la cadena de conexión

---

## Configuración de la base de datos

### 1. MongoDB Atlas

Crear en MongoDB Atlas una base de datos llamada **`Tienda`** con dos colecciones:

- **`items`** – Artículos de la tienda
- **`users`** – Usuarios para autenticación (admin y usuarios normales)

### 2. Estructura del documento Item (colección `items`)

```json
{
  "id": 1,
  "title": "Nombre del artículo",
  "price": 29.99,
  "category": "Electrónica",
  "description": "<p>Descripción (puede incluir HTML)</p>",
  "rate": 8.5,
  "count": 100,
  "manufacturer": "Fabricante",
  "color": "Negro",
  "EAN": "1234567890123",
  "image": "https://ejemplo.com/imagen.jpg",
  "stock": 50
}
```

| Campo        | Tipo    | Descripción                                      |
|-------------|---------|--------------------------------------------------|
| id          | Integer | Identificador único interno (obligatorio)        |
| title       | String  | Nombre del artículo                              |
| price       | Double  | Precio                                           |
| category    | String  | Categoría                                        |
| description | String  | Descripción (puede incluir HTML)                 |
| rate        | Double  | Valoración media (1-10)                          |
| count       | Integer | Veces vendido                                    |
| manufacturer| String  | Fabricante                                       |
| color       | String  | Color                                            |
| EAN         | String  | Código de barras                                 |
| image       | String  | URL de la imagen                                 |
| stock       | Integer | Unidades en almacén (para estadísticas)          |

El campo `_id` lo genera MongoDB automáticamente.

### 3. Usuarios (colección `users`)

```json
[
  {
    "email": "admin@tienda.com",
    "password": "admin123",
    "role": "ADMIN"
  },
  {
    "email": "usuario@tienda.com",
    "password": "usuario123",
    "role": "USER"
  }
]
```

- **ADMIN:** puede añadir, eliminar ítems y cambiar categorías.
- **USER:** solo puede consultar (no modificar datos).

### 4. Variable de entorno

Antes de ejecutar, definir la variable de entorno:

```bash
# Windows (PowerShell)
$env:MONGODB_URI="mongodb+srv://usuario:contraseña@cluster.mongodb.net/"

# Linux / macOS
export MONGODB_URI="mongodb+srv://usuario:contraseña@cluster.mongodb.net/"
```

---

## Ejecución

```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La API estará disponible en **http://localhost:8080**.

---

## Historias de usuario y endpoints

### Resumen

| Historia | Descripción                      | Método | Endpoint                      | Auth   |
|----------|----------------------------------|--------|-------------------------------|--------|
| A        | Agregar un nuevo ítem           | POST   | `/api/items`                  | ADMIN  |
| B        | Eliminar un ítem                | DELETE | `/api/items/{id}`             | ADMIN  |
| C        | Obtener detalles de un ítem     | GET    | `/api/items/{id}`             | No     |
| D        | Listar ítems por categoría      | GET    | `/api/items?category=...`     | No     |
| E        | Cambiar categoría               | PUT    | `/api/items/category`         | ADMIN  |
| F        | Estadísticas de la tienda       | GET    | `/api/stats`                  | No     |

---

## Guía de pruebas

### Autenticación

Las operaciones de escritura usan **Basic Auth**:

- **Usuario admin:** `admin@tienda.com` / `admin123`
- **Header Base64:** `Authorization: Basic YWRtaW5AdGllbmRhLmNvbTphZG1pbjEyMw==`

---

### A. Agregar un nuevo ítem

```http
POST http://localhost:8080/api/items
Content-Type: application/json
Authorization: Basic YWRtaW5AdGllbmRhLmNvbTphZG1pbjEyMw==

{
  "id": 200,
  "title": "Camiseta Básica",
  "price": 19.99,
  "category": "ropa",
  "description": "<p>Camiseta de algodón</p>",
  "rate": 4.5,
  "count": 0,
  "manufacturer": "Springfield",
  "color": "negro",
  "EAN": "8436569570011",
  "image": "https://ejemplo.com/imagen.jpg",
  "stock": 150
}
```

**Respuesta:** 200 OK con el ítem guardado (incluye `_id`).

---

### B. Eliminar un ítem

```http
DELETE http://localhost:8080/api/items/200
Authorization: Basic YWRtaW5AdGllbmRhLmNvbTphZG1pbjEyMw==
```

**Respuesta:** 204 No Content.

---

### C. Obtener detalles de un ítem

```http
GET http://localhost:8080/api/items/1
```

**Respuesta:** 200 OK con el JSON del ítem. 404 si no existe.

---

### D. Listar ítems por categoría

```http
GET http://localhost:8080/api/items?category=ropa
```

**Respuesta:** 200 OK con un array de ítems de esa categoría.

---

### E. Cambiar categoría

```http
PUT http://localhost:8080/api/items/category
Content-Type: application/json
Authorization: Basic YWRtaW5AdGllbmRhLmNvbTphZG1pbjEyMw==

{
  "oldCategory": "ropa",
  "newCategory": "Ropa"
}
```

**Respuesta:** 200 OK. Actualiza la categoría en todos los ítems que la tenían.

---

### F. Estadísticas de la tienda

```http
GET http://localhost:8080/api/stats
```

**Respuesta:**
```json
{
  "totalItems": 10,
  "itemsWithLowStock": 2,
  "manufacturers": ["Fabricante1", "Fabricante2"]
}
```

- `totalItems`: total de ítems
- `itemsWithLowStock`: ítems con stock < 100
- `manufacturers`: lista de fabricantes

---

## Herramientas de prueba

- **IntelliJ IDEA:** archivo `.http` o pestaña HTTP Client
- **Postman / Insomnia:** importar requests con Basic Auth
- **curl:**

```bash
# Obtener ítem (sin auth)
curl http://localhost:8080/api/items/1

# Estadísticas
curl http://localhost:8080/api/stats

# Agregar ítem (con auth)
curl -X POST http://localhost:8080/api/items -u admin@tienda.com:admin123 \
  -H "Content-Type: application/json" \
  -d '{"id":99,"title":"Test","price":10,"category":"Test","description":"x","rate":5,"count":0,"manufacturer":"X","color":"rojo","EAN":"123","image":"http://x.com","stock":50}'
```

---

## Gestión de errores

- **401 Unauthorized:** credenciales incorrectas o ausentes
- **403 Forbidden:** usuario sin rol ADMIN en operaciones restringidas
- **404 Not Found:** ítem no encontrado
- **400 Bad Request:** datos inválidos (p. ej. `id` duplicado o vacío)

Los errores se devuelven en JSON con `error`, `message` y `errorCode`.
