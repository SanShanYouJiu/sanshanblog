FROM openjdk:8-jre-slim
VOLUME /tmp
COPY sanshan-main-web.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
COPY config.json /etc/sanshan-main/config.json
COPY sanshan-main-setting.json /etc/sanshan-main/sanshan-main-setting.json
RUN bash -c 'touch /app.jar' && mkdir /etc/cache-data
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]