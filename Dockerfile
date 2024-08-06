FROM maven:3.9-eclipse-temurin-21-alpine AS BUILDER
WORKDIR app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21.0.1_12-jre-alpine AS LAYERS
WORKDIR layer
ARG JAR_FILE=GithubRepositoriesList-0.0.1-SNAPSHOT.jar
COPY --from=BUILDER /app/target/$JAR_FILE app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21.0.1_12-jre-alpine
WORKDIR application
EXPOSE 8080
COPY --from=LAYERS layer/dependencies ./
COPY --from=LAYERS layer/spring-boot-loader ./
COPY --from=LAYERS layer/snapshot-dependencies ./
COPY --from=LAYERS layer/application ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]