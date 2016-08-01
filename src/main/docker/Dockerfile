FROM java:8
MAINTAINER abhi.bhuj@gmail.com
VOLUME /tmp
ADD notification-service.jar app.jar
EXPOSE 8080
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]