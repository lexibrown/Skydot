#!/bin/bash

kubectl get nodes --show-labels

#kubectl apply -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/balance.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/host-gateway.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/transfer-service-spring.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/jumpod.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/auth-app.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/mobile-app.yaml
#kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/web-app.yaml

kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/account-details-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/account-summary-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payee-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payee-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payee-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payment-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payment-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payment-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/bill-payment-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/create-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/create-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/delete-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/delete-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/transfer-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/transfer-service-java.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/transfer-service-javascript.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/transfer-service-python.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/verify-service-cplus.yaml
kubectl create -f https://raw.githubusercontent.com/lexibrown/Skydot/master/YAML/v2/verify-service-python.yaml

kubectl get pods -o wide