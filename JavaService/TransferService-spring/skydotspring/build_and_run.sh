docker build -t transfer-service-spring .
docker run -d -p 80:8080 transfer-service-spring