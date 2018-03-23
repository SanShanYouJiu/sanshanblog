FROM openjdk:8-jre-slim
VOLUME /tmp
COPY sanshan-config.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]