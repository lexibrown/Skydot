docker build -t transfer-service-javascript .
docker run -d -p 80:80 transfer-service-javascript