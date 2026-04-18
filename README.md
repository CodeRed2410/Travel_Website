# TripNext - Deployment Ready

This project is configured to run:
- Locally with H2 (no setup needed)
- Online with PostgreSQL (Neon/Supabase/Railway/Render)

## 1. Run locally

```bash
mvn spring-boot:run
```

## 2. Run online (PostgreSQL)

Set these environment variables in your hosting platform:

- `DB_URL` (example: `jdbc:postgresql://<host>:5432/<db>?sslmode=require`)
- `DB_USERNAME`
- `DB_PASSWORD`
- `PORT` (hosting platform usually sets this automatically)

Then start app:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Or build jar:

```bash
mvn clean package
java -jar target/travel-site-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 3. Quick Render/Railway setup

1. Push this repo to GitHub.
2. Create new Web Service from repo.
3. Build command:
   `mvn clean package`
4. Start command:
   `java -jar target/travel-site-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`
5. Add env vars: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

## Notes

- Schema auto-update is enabled (`spring.jpa.hibernate.ddl-auto=update`) for demo convenience.
- H2 console is disabled for safety.
