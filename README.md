# BellBooks-backend

## **To deploy code to GCP App Engine**

- build the project (npm run build, ./gradlew build)
- for frontend, `gcloud app deploy --project bellbooks-frontend build/app.yaml`
- for backend, delete the existing jar from main>appengine, and move the generated jar from build>libs into the main>appengine
- then,  `gcloud app deploy --project bellbooks-backend src/main/appengine/app.yaml`


## Running Tests

Requires Java 17. If your system default is a different version, point Gradle at Java 17:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew test
```

To run only the service unit tests:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew test --tests "com.cocosmaj.BellBooks.service.*"
```

> **Note:** The `BellBooksApplicationTests.contextLoads()` test requires GCP credentials and will fail locally. The service unit tests run independently without any database or cloud connection.

#### Controller conventions

/add<entity>

/get<entity>

/getAll<entity>

/delete<entity>

/update<entity>
