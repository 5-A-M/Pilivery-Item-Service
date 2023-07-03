FROM openjdk:11
WORKDIR /app
COPY /build/libs/item-service-1.0.jar itemSVC.jar
CMD ["java", "-jar", "/app/itemSVC.jar"]
