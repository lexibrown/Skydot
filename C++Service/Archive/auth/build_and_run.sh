docker build -t verify-service-cplus .
docker run -d -p 80:80 verify-service-cplus