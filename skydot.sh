#!/bin/bash


kubectl get nodes --show-labels


#kubectl apply -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/balance.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/host-gateway.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/transfer-service-spring.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/jumpod.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/auth-app.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/mobile-app.yaml
#kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/web-app.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/account-details-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/account-summary-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payee-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payee-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payee-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payment-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payment-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payment-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/bill-payment-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/create-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/create-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/delete-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/delete-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/transfer-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/transfer-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/transfer-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/transfer-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/verify-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/verify-service-python.yaml

kubectl get pods -o wide


echo "Testing services"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://verify-service-python:8080/auth
kubectl exec jumpod -c shell -i -t -- curl -X POST http://auth-app:8080/auth/login