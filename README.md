# hmpps-prisoner-cell-allocation-api
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://cell-allocation-api-dev.prison.service.justice.gov.uk/swagger-ui.html)

This service provides:
* Cell allocation functionality

# Starting the service locally
```docker-compose up``` will bring local database up. 

Setup environment variables in your IDE following docker-compose.yml ```SPRING_DATASOURCE_USERNAME=test;SPRING_DATASOURCE_PASSWORD=test;DATABASE_NAME=hmpps-prisoner-cell-allocation-api-db;DATABASE_ENDPOINT=localhost:5431```
Run the `App.kt` main method with `dev` profile.


Ensure that IntelliJ is configured to run and build the project using gradle rather than it's own build tool.

### Linting
to run linting  ```./gradlew ktlintFormat```
