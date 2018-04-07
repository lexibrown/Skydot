docker build -t bill-payment-service-java .
docker run -d -p 80:8080 bill-payment-service-java