docker build -t bill-payee-service-java .
docker run -d -p 80:8080 bill-payee-service-java