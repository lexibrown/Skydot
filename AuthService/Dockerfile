FROM lex13/skydot
WORKDIR /auth
RUN git clone https://github.com/lexibrown/Skydot.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /auth
COPY --from=0 /app/spring-petclinic /app (2)
RUN mvn install (3)

FROM openjdk:8-jre-alpine
WORKDIR /auth
COPY --from=1 /app/target/spring-petclinic-1.5.1.jar /app (4)
CMD ["java -jar spring-petclinic-1.5.1.jar"] (5)
