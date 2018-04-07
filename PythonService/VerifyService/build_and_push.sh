docker build -t verify-service-python .
docker tag verify-service-python lex13/verify-service-python
docker push lex13/verify-service-python