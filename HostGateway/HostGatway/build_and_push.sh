docker build -t host-gateway .
docker tag host-gateway lex13/host-gateway
docker push lex13/host-gateway