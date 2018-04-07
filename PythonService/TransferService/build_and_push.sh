docker build -t transfer-service-python .
docker tag transfer-service-python lex13/transfer-service-python
docker push lex13/transfer-service-python