docker build -t auth-app .
docker run -d -p 80:80 auth-app