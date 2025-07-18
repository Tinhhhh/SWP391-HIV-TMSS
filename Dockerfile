# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests=true

# Run Stage
FROM eclipse-temurin:17-jre-ubi9-minimal
WORKDIR /run
COPY --from=build /app/target/HIV-TMSS-0.0.1-SNAPSHOT.jar /run/HIV-TMSS-0.0.1-SNAPSHOT.jar
EXPOSE 8888
ENTRYPOINT java -jar /run/HIV-TMSS-0.0.1-SNAPSHOT.jar


