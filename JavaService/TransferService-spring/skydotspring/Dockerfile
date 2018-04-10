FROM java:alpine
VOLUME /tmp
ADD target/skydot-spring-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

## - to remove old images
# docker ps --all
# docker kill <name of running images>
# docker system prune -a
#
## - to build and run
# docker build -t transferspring .
# docker run -d -p 8080:8080 transferspring
#
## - to fget rid of stupid Mac index files before a build
# sudo find ./ -name ".DS_Store" -depth -exec rm {} \;
