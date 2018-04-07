docker build -t transfer-service-java .
docker tag transfer-service-java lex13/transfer-service-java
docker push lex13/transfer-service-java