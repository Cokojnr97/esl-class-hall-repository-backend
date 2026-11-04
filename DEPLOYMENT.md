# Deployment

## Run locally with the external PostgreSQL database

1. Create a local `.env` file from `.env.example`.
2. Set `DB_URL` to the JDBC form of the PostgreSQL connection URL:

   ```text
   jdbc:postgresql://pooled.db.prisma.io:5432/postgres?sslmode=require
   ```

3. Set `DB_USERNAME` and `DB_PASSWORD` to the credentials supplied by the
   database provider. Do not commit `.env`.
4. Start the API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

The API listens on `http://localhost:8080` by default. The OpenAPI UI is
available at `/swagger-ui.html`, and Scalar is available at `/scalar`.

## Deploy with Back4app Containers

Back4app Containers builds the repository's `Dockerfile`, so no Docker Desktop
instance is required for the deployed application.

1. Push this repository to GitHub. Do not push `.env` or any database
   credentials.
2. In Back4app, open **Containers**, choose **Create new app**, connect the
   GitHub repository, and select the deployment branch.
3. Add these environment variables in the container settings:

   | Variable | Value |
   | --- | --- |
   | `DB_URL` | `jdbc:postgresql://pooled.db.prisma.io:5432/postgres?sslmode=require` |
   | `DB_USERNAME` | The database username |
   | `DB_PASSWORD` | The database password |
   | `PORT` | `8080` |

   Enter the password in Back4app's environment-variable form; do not put it
   in the repository or Dockerfile.
4. Deploy the container. Back4app will build the image and provide a public
   application URL after startup.
5. Verify the deployment with:

   ```text
   https://YOUR-BACK4APP-URL/swagger-ui.html
   ```

The application binds to the port from `PORT` and defaults to `8080`, which
matches the Dockerfile's exposed port.

## Database and security notes

- The database URL must use the `jdbc:postgresql:` prefix when supplied to
  Spring Boot.
- `spring.jpa.hibernate.ddl-auto=update` lets Hibernate create or update the
  schema on startup. Use migrations before production if the schema must be
  controlled and repeatable.
- Rotate any database password that has been shared in chat, committed to Git,
  or placed in a local file that was previously pushed. Updating `.env` does
  not remove old credentials from Git history.
- If the database provider offers separate pooled and direct connection URLs,
  use the provider's pooled URL for the application and keep SSL enabled.
