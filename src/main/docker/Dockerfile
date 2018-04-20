FROM java:8
VOLUME /tmp
ADD data-transfer.war app.war
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]
EXPOSE 8080