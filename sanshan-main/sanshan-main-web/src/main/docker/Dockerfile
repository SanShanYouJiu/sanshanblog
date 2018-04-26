FROM openjdk:8-jre-slim
VOLUME /tmp
COPY sanshan-main-web.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
COPY config.json /etc/sanshanblog
COPY sanshanblog-setting.json /etc/sanshanblog
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]