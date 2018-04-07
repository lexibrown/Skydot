docker build -t transfer-service-javascript .
docker tag transfer-service-javascript lex13/transfer-service-javascript
docker push lex13/transfer-service-javascript