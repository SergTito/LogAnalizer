# Используем официальный образ OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/log-processor-*.jar log-processor.jar
EXPOSE 8082
# Указываем команду для запуска сервиса
ENTRYPOINT ["java", "-jar", "log-processor.jar"]
