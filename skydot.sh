#!/bin/bash

kubectl get nodes --show-labels

#kubectl apply -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/balance.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/host-gateway.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/transfer-service-spring.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/jumpod.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/auth-app.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/mobile-app.yaml
#kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/web-app.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/account-details-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/account-summary-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payee-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payee-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payee-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payment-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payment-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payment-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/bill-payment-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/create-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/create-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/delete-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/delete-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/transfer-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/transfer-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/transfer-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/transfer-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/verify-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v1/verify-service-python.yaml

kubectl get pods -o wide