docker build -t auth-app .
docker tag auth-app lex13/auth-app
docker push lex13/auth-app