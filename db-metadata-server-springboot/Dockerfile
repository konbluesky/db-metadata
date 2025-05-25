# 第一阶段：构建
FROM maven:3.8.4-openjdk-17 AS builder

# 设置工作目录
WORKDIR /build

# 复制所有项目文件
COPY ../db-metadata-parent /build/db-metadata-parent
COPY ../db-metadata-analysis-springboot /build/db-metadata-analysis-springboot
COPY ./db-metadata-server-springboot /build/db-metadata-server-springboot

WORKDIR /build/db-metadata-parent
RUN mvn clean install -DskipTests

# 第二阶段：运行
FROM eclipse-temurin:17-jre

WORKDIR /app

# 从构建阶段复制构建好的jar文件
COPY --from=builder /build/db-metadata-server-springboot/target/*.jar ./app.jar

# 暴露端口
EXPOSE 8080

# 设置启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]