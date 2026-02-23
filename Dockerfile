# Giai đoạn 1: Build file .jar bằng Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Lệnh này sẽ đóng gói code Java thành file .jar
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy ứng dụng (Dùng JRE cho nhẹ)
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy file .jar từ giai đoạn build sang đây
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Sử dụng exec form + shell expansion để đọc biến môi trường
ENTRYPOINT ["java", "-jar", "app.jar"]