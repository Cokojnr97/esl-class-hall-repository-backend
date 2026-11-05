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

## Development mock data

On startup, the development seed adds the following idempotently:

- The default administrator, when it does not already exist.
- 10 teachers with realistic Colombian names, profiles, phone numbers, and
  `@cesde.net` email addresses.
- 40 published resources with realistic titles, descriptions, educational URLs,
  resource types, course levels, and teacher ownership.

Existing data is not replaced. Legacy placeholder records created by an earlier
version of the seed are updated in place. The seeded users use passwords
`mock-password-1` through `mock-password-10`; use them only in development.
