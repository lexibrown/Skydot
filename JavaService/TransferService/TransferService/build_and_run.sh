docker build -t transfer-service-java .
docker run -d -p 80:8080 transfer-service-java