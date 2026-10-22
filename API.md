# ESL Class Hall Repository API

## Interactive documentation

With the application running:

- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Scalar: `http://localhost:8080/scalar`

## REST resources

All identifiers are UUIDs.

| Resource | Route | Operations |
| --- | --- | --- |
| Users | `/api/usuarios` | GET, POST, PUT, DELETE |
| Administrators | `/api/admins` | GET, POST, PUT, DELETE |
| Teachers | `/api/docentes` | GET, POST, PUT, DELETE |
| Profiles | `/api/perfiles` | GET, POST, PUT, DELETE |
| Courses | `/api/cursos` | GET, POST, PUT, DELETE |
| Digital resources | `/api/recursos` | GET, POST, PUT, DELETE |
| Requests | `/api/solicitudes` | GET, POST, PUT, DELETE |

Additional queries:

- `GET /api/usuarios/email/{email}`
- `GET /api/cursos?docenteId={uuid}`
- `GET /api/recursos/publicados`

## Request notes

For existing relationships, send nested objects containing only their `id`, for example:

```json
{
  "titulo": "Present perfect exercises",
  "descripcion": "Guided practice material",
  "url": "https://example.com/material.pdf",
  "tipo": "DOCUMENTO",
  "creadoPor": {
    "id": "00000000-0000-0000-0000-000000000000"
  },
  "cursos": []
}
```

The development seed runs only when the database contains no users. It does not replace existing data.
