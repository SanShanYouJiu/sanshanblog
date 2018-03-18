FROM openjdk:8-jre-slim
VOLUME /tmp
COPY sanshan-main-web-1.0-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]