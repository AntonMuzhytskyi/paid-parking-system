# Этап 1: Сборка приложения
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Копируем только pom.xml сначала (для кэширования зависимостей)
COPY pom.xml .

# Скачиваем зависимости (это закешируется, если pom не менялся)
RUN ./mvnw dependency:go-offline

# Копируем исходный код
COPY src ./src

# Собираем JAR (skip tests для скорости, если хочешь — убери -DskipTests)
RUN ./mvnw clean package -DskipTests

# Этап 2: Запуск приложения (минимальный образ)
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Копируем только собранный JAR из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]