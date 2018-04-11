
echo "account-details-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://account-details-service-python/account/details
echo
echo "account-summary-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://account-summary-service-python/account/summary

echo
echo "auth-app"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://auth-app/auth/login

echo
echo "bill-payee-service-java"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payee-service-java/bill/payee/search -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "bill-payee-service-javascript"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payee-service-javascript/bill/payee/search -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "bill-payee-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payee-service-python/bill/payee/search -d '{"token" : "100"}' -H "Content-Type: application/json"

echo
echo "bill-payment-service-java"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payment-service-java/bill -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "bill-payment-service-javascript"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payment-service-javascript/bill -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "bill-payment-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payment-service-python/bill -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "bill-payment-service-cplus"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://bill-payment-service-cplus/bill -d '{"token" : "100"}' -H "Content-Type: application/json"

echo
echo "create-service-cplus"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://create-service-cplus/create
echo
echo "create-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://create-service-python/create

echo
echo "delete-service-cplus"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://delete-service-cplus/delete
echo
echo "delete-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://delete-service-python/delete

echo
echo "host-gateway"
kubectl exec jumpod -c shell -i -t -- curl http://host-gateway/host

echo
echo "mobile-app"
kubectl exec jumpod -c shell -i -t -- curl http://mobile-app/account

echo
echo "transfer-service-java"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://transfer-service-java/transfer -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "transfer-service-javascript"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://transfer-service-javascript/transfer -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "transfer-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://transfer-service-python/transfer -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "transfer-service-cplus"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://transfer-service-cplus/transfer -d '{"token" : "100"}' -H "Content-Type: application/json"
echo
echo "transfer-service-spring"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://transfer-service-spring/transfer -d '{"token" : "100"}' -H "Content-Type: application/json"

echo
echo "verify-service-python"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://verify-service-python/auth
echo
echo "verify-service-cplus"
kubectl exec jumpod -c shell -i -t -- curl -X POST http://verify-service-cplus/auth


