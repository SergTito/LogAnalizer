# Используем официальный образ OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/log-api-*.jar log-api.jar

# Указываем команду для запуска сервиса
ENTRYPOINT ["java", "-jar", "log-api.jar"]
