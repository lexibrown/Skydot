docker build -t verify-service-python .
docker run -d -p 80:80 verify-service-python