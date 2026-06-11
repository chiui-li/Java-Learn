# ===== Stage 1: 构建阶段 =====
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# 构建 JAR（跳过测试以加快构建）
RUN mvn clean package -DskipTests -q

# ===== Stage 2: 运行阶段 =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 从 builder 阶段复制构建产物
COPY --from=builder /app/target/*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 入口
ENTRYPOINT ["java", "-jar", "app.jar"]
