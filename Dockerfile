FROM gradle:6.6.1-jdk11-hotspot as builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle build --stacktrace

FROM adoptopenjdk/openjdk11:jre
WORKDIR /app
EXPOSE 9999
COPY  --from=builder /builder/build/libs/request-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "request-0.0.1-SNAPSHOT.jar"]
