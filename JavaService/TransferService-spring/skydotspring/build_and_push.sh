docker build -t transfer-service-spring .
docker tag transfer-service-spring lex13/transfer-service-spring
docker push lex13/transfer-service-spring