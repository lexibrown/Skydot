FROM jetty:alpine

COPY ./target/MobileApp.war /var/lib/jetty/webapps/ROOT.war

CMD java -jar /usr/local/jetty/start.jar jetty.http.port=8080 jetty.ssl.port=800