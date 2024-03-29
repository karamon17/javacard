# Используем официальный образ Maven с JDK 17 как базовый
FROM maven:3.8.1-openjdk-17-slim as build

# Устанавливаем рабочую директорию в /app
WORKDIR /app

# Копируем pom.xml
COPY pom.xml .

# Копируем исходный код в рабочую директорию
COPY src /app/src

# Собираем приложение
RUN mvn clean package

# Используем официальный образ OpenJDK 17 JRE для запуска приложения
FROM openjdk:17-slim

# Копируем собранный jar файл из предыдущего образа
COPY --from=build /app/target/*.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java","-jar","/app.jar"]