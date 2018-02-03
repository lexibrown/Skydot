FROM jetty:alpine

COPY ./target/AuthService.war /var/lib/jetty/webapps/

CMD java -jar /usr/local/jetty/start.jar jetty.http.port=8000 jetty.ssl.port=800