# BellBooks-backend

## **To deploy code to GCP App Engine**

- build the project (npm run build, ./gradlew build)
- for frontend, `gcloud app deploy --project bellbooks-frontend build/app.yaml`
- for backend, delete the existing jar from main>appengine, and move the generated jar from build>libs into the main>appengine
- then,  `gcloud app deploy --project bellbooks-backend src/main/appengine/app.yaml`


#### Controller conventions

/add<entity>

/get<entity>

/getAll<entity>

/delete<entity>

/update<entity>
