FROM eclipse-temurin:25-jre-alpine AS training
WORKDIR /app

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN java -XX:ArchiveClassesAtExit=app.jsa \
         -Dspring.profiles.active=aotbuild \
         -Dspring.aot.enabled=true \
         -Dspring.context.exit-on-refresh=true \
         -jar app.jar || true

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl
RUN addgroup -S spring && adduser -S spring -G spring

#Wird durch Github befüllt, muss ggf geändert werden falls das local gebraucht wird ?!
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
COPY --from=training /app/app.jsa app.jsa

USER spring:spring

ENTRYPOINT ["java", \
            "-XX:SharedArchiveFile=app.jsa", \
            "-Dspring.aot.enabled=true", \
            "-jar", "app.jar"]