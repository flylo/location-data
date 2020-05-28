FROM openjdk:11
COPY location-data-service/target target
CMD ["java", "-jar", "target/location-data-service-1.0-SNAPSHOT.jar", "server"]
