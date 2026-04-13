# 基础镜像：Java 17（和项目JDK版本一致）
FROM eclipse-temurin:17.0.18_8-jre

# 维护者信息（可选）
LABEL maintainer="wangchao <wangchao000413@qq.com>"

# 设定时区（避免时间错乱）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建工作目录（容器内的）
WORKDIR /app

# 将本地jar包复制到容器内（注意替换成你的jar包名称）
COPY target/qqspeed-data-api-1.0.0.jar /app/qqspeed-car.jar

# 暴露端口（和application-dev.yml中server.port一致，默认8080）
EXPOSE 8080

# 容器启动命令（运行jar包）
ENTRYPOINT ["java","-jar","/app/qqspeed-car.jar","--spring.profiles.active=dev"]