docker build -t host-gateway .
docker run -d -p 80:8080 host-gateway