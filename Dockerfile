FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

#Wird durch Github befüllt, muss ggf geändert werden falls das local gebraucht wird ?!
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]