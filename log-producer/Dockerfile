# Используем официальный образ OpenJDK 17 с JDK
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл в контейнер (его нужно будет собрать заранее)
COPY target/log-producer-*.jar log-producer.jar
EXPOSE 8083
# Указываем команду для запуска сервиса
ENTRYPOINT ["java", "-jar", "log-producer.jar"]
