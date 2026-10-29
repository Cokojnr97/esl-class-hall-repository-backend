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

Write endpoints use request DTOs and return response DTOs. Nested references in requests contain an `id`; responses contain the reference `id` and display name. User responses never include `passwordHash`. Resource status and request-decision fields are managed by the approval workflow and are not accepted in write DTOs.

Course request example:

```json
{
  "nombre": "Present perfect",
  "descripcion": "Guided practice",
  "orden": 1,
  "docente": {
    "id": "00000000-0000-0000-0000-000000000000"
  }
}
```

Request bodies are validated for required values, field lengths, and email format. Validation errors return HTTP 400 with a `mensaje` field.

When approving a `CREAR` request without `recursoObjetivo`, `payloadJson` must contain the resource data (`titulo`, `descripcion`, `url`, `tipo` and optional `cursos` references). Approving an `ACTUALIZAR` request applies the same payload fields to its target resource before publishing it.

The development seed is disabled by default. Set `SEED_DATA=true` to enable it; it only runs when the database contains no users and does not replace existing data. Configure browser origins with `CORS_ALLOWED_ORIGINS` as a comma-separated list (by default, local development origins on ports 3000 and 5173 are allowed).
