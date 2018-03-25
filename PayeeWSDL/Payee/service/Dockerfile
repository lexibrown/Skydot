FROM jetty:alpine 

COPY ./target/skydot-payee-service-1.0.0.war /var/lib/jetty/webapps/skydot.war

CMD java -Dbanking.home=/banking-home -jar /usr/local/jetty/start.jar jetty.http.port=8680 jetty.ssl.port=8643

## - to remove old images
# docker ps --all
# docker kill <name of running images>
# docker system prune -a
#
## - to build and run
# docker build -t payees .
# docker run -d -p 8680:8680 -p 8643:8643 payees
#
## - WSDL to import into SoapUI:
# <project directory>/Payee/wsdl/PaymentService.wsdl
## - SoapUI/curl endpoint:
# http://localhost:8680/skydot/payees
#
## - to fget rid of stupid Mac index files before a build
# sudo find ./ -name ".DS_Store" -depth -exec rm {} \;
