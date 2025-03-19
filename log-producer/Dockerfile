# Используем OpenJDK 17
FROM openjdk:17

# Указываем рабочую директорию в контейнере
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/log-producer-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
