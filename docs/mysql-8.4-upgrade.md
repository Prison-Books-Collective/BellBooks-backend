# Cloud SQL MySQL 8.0 → 8.4 upgrade runbook

## Why
`bellbooks-sql-instance` runs MySQL 8.0. Cloud SQL ends standard support for 8.0 on **Jan 1, 2027**, after which the
instance is billed for *extended support* and stops getting new features (Google force-upgrades it at deprecation).
Nothing breaks on that date, but we want to move to **MySQL 8.4** on our own schedule.

The frontend never talks to MySQL. It only calls this backend over HTTP, so the frontend needs no changes.
The risk is only that the backend fails to connect after the upgrade, which shows up in the UI as errors or empty lists.

## What changed in the backend (already done in code)
| Change | Why |
|---|---|
| Cloud SQL connector `mysql-socket-factory-connector-j-8` 1.2.2 → 1.31.0 | 1.2.2 dates from 2021, long before MySQL 8.4. |
| Driver `mysql:mysql-connector-java:8.0.30` → `com.mysql:mysql-connector-j:26.10.0` | Old artifact coordinates; 26.10.0 is what the connector is built and tested against. |
| Removed unused `spring-cloud-gcp-starter` + its BOM | Nothing used it. Its 2022 BOM forced the connector's core library back to 1.8.0, a broken mismatch. |
| Guava pin `32.0.0-android` → `33.8.0-jre` | The pin was forcing the connector's Guava 33 dependency down to 32. |
| Hibernate dialect `MySQL5Dialect` → `MySQL8Dialect` | Match the server. Verified: restarting against an existing schema emits **no** DDL with either dialect. |
| JDBC URL no longer sets `useSSL=false`, `user=root`, `password=` | MySQL 8.4 requires `caching_sha2_password`, which **refuses to log in** without TLS (verified locally: `Public Key Retrieval is not allowed`). The connector already encrypts the tunnel, so TLS is fine. |
| Credentials come from `DB_USER` / `DB_PASS` env vars | Stop connecting as `root` with an empty password. App Engine reads them from `src/main/appengine/db-credentials.yaml` (gitignored). |

All of these work on the **current 8.0** instance, so the backend ships first and the database upgrade follows.

## Steps (run in order)

Every `gcloud` command below takes `--project=bellbooks-backend`. Cloud Shell is the easiest place to run them.

### 1. Create a dedicated app user (MySQL 8.0, before deploying the new backend)
Connect as root (`gcloud sql connect bellbooks-sql-instance --user=root --project=bellbooks-backend`), then:

```sql
-- See which accounts still use the deprecated plugin; the 8.4 precheck flags these.
SELECT user, host, plugin FROM mysql.user;

CREATE USER 'bellbooks-app'@'%' IDENTIFIED WITH caching_sha2_password BY '<strong password>';
GRANT ALL PRIVILEGES ON migration.* TO 'bellbooks-app'@'%';   -- ddl-auto=update needs CREATE/ALTER/INDEX
```

Give `root` a real password and move it off `mysql_native_password`:

```sql
ALTER USER 'root'@'%' IDENTIFIED WITH caching_sha2_password BY '<another strong password>';
```

Do the same for any other account the `SELECT` showed with `mysql_native_password`.

### 2. Deploy the updated backend (still on MySQL 8.0)
1. `cp src/main/appengine/db-credentials.example.yaml src/main/appengine/db-credentials.yaml`, then fill in the password from step 1. **Never commit this file.**
2. Build and deploy as in the README. Gradle 7.5 needs **JDK 17**; it fails on JDK 21.
3. Click through the frontend: facility list, recipient search, add a book by ISBN, create a shipment.

### 3. Rehearse the upgrade on a clone
```bash
gcloud sql backups create --instance=bellbooks-sql-instance --project=bellbooks-backend
```
```bash
gcloud sql instances clone bellbooks-sql-instance bellbooks-sql-84-test --project=bellbooks-backend
```
Run the major-version upgrade **precheck** on the clone (Console → SQL → `bellbooks-sql-84-test` → Upgrade), fix anything
it reports, then upgrade the clone:
```bash
gcloud sql instances patch bellbooks-sql-84-test --database-version=MYSQL_8_4 --project=bellbooks-backend
```
Point a local backend at the clone. Your account needs the *Cloud SQL Client* role; run `gcloud auth application-default login` first.
```bash
DB_USER=bellbooks-app DB_PASS='<password>' java -jar build/libs/BellBooks-0.0.1-SNAPSHOT.jar "--spring.datasource.url=jdbc:mysql:///migration?cloudSqlInstance=bellbooks-backend:us-central1:bellbooks-sql-84-test&socketFactory=com.google.cloud.sql.mysql.SocketFactory"
```
Check that it starts, that the log shows no `create table` / `alter table ... add column` lines, and that the API works,
for example by pointing a local frontend at `http://localhost:8080`.
If login fails with `Public Key Retrieval is not allowed`, add `&allowPublicKeyRetrieval=true` to the URL in
`application.properties` and retest.

Delete the clone when done:
```bash
gcloud sql instances delete bellbooks-sql-84-test --project=bellbooks-backend
```

### 4. Upgrade production
Pick a quiet time; the upgrade takes the database offline for a few minutes.
```bash
gcloud sql backups create --instance=bellbooks-sql-instance --project=bellbooks-backend
```
```bash
gcloud sql instances patch bellbooks-sql-instance --database-version=MYSQL_8_4 --project=bellbooks-backend
```
Confirm the instance reports `MYSQL_8_4`, the App Engine logs show no connection errors, and the frontend smoke test passes.
```bash
gcloud sql instances describe bellbooks-sql-instance --project=bellbooks-backend --format='value(databaseVersion)'
```

**Rollback:** a major-version upgrade can't be undone in place. Restore the pre-upgrade backup to a new 8.0 instance and
change `cloudSqlInstance` in `application.properties` to point at it. This is why step 3 matters.

## References
- Version and support dates: https://docs.cloud.google.com/sql/docs/mysql/db-versions
- Extended support: https://docs.cloud.google.com/sql/docs/mysql/extended-support
- In-place major upgrade (8.4 auth changes, precheck): https://docs.cloud.google.com/sql/docs/mysql/upgrade-major-db-version-inplace
