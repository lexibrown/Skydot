docker build -t transfer-service-python .
docker run -d -p 80:80 transfer-service-python