
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app


COPY pom.xml .


COPY common/pom.xml common/
COPY department-module/pom.xml department-module/
COPY doctor-module/pom.xml doctor-module/
COPY patient-module/pom.xml patient-module/
COPY appointment-module/pom.xml appointment-module/
COPY medical-records-module/pom.xml medical-records-module/
COPY notification-module/pom.xml notification-module/
COPY security-module/pom.xml security-module/


RUN mvn dependency:go-offline -B

COPY common/src common/src
COPY department-module/src department-module/src
COPY doctor-module/src doctor-module/src
COPY patient-module/src patient-module/src
COPY appointment-module/src appointment-module/src
COPY medical-records-module/src medical-records-module/src
COPY notification-module/src notification-module/src
COPY security-module/src security-module/src

RUN mvn clean package -DskipTests -B


FROM eclipse-temurin:17-jre-alpine


WORKDIR /app

COPY --from=build /app/security-module/target/security-module-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]