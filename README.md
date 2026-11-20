# BellBooks-backend

## **To deploy code to GCP App Engine**

- build the project (npm run build, ./gradlew build) — the backend build needs JDK 17 (Gradle 7.5 fails on JDK 21)
- backend only: `src/main/appengine/db-credentials.yaml` must exist (copy `db-credentials.example.yaml` and fill in the DB user/password; never commit it)
- for frontend, `gcloud app deploy --project bellbooks-frontend build/app.yaml`
- for backend, delete the existing jar from main>appengine, and move the generated jar from build>libs into the main>appengine
- then,  `gcloud app deploy --project bellbooks-backend src/main/appengine/app.yaml`

See `docs/mysql-8.4-upgrade.md` for the Cloud SQL MySQL 8.0 → 8.4 upgrade runbook.


#### Controller conventions

/add<entity>

/get<entity>

/getAll<entity>

/delete<entity>

/update<entity>
