from flask import Flask, request, abort, jsonify, Response, json
import logging, sys, requests

logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)

app = Flask(__name__)

@app.errorhandler(400)
def invalid(error=None):
	message = {
			'status': 400,
			'error': 'AUTH-1000',
			'message': 'Invalid request'
	}
	resp = jsonify(message)
	resp.status_code = 400
	return resp

@app.route('/test3', methods = ['GET'])
def verify3():
	content = {
			'user': 1234,
			'password': 'password'
	}
		
	url = 'http://verify-service-python:8080/auth'
	
	logging.debug(url)
	
	response = requests.post(url = url, json = content)
	js = json.dumps(response.json())

	logging.debug(js)
	
	if 'error' in js:
		return Response(js, status = 401, mimetype = 'application/json')
	
	return Response(js, status = 200, mimetype = 'application/json')

@app.route('/test2', methods = ['GET'])
def verify2():
	content = {
			'user_id': 'username',
			'password': 'password'
	}
		
	url = 'http://auth:5000/auth/login'
	
	logging.debug(url)
	
	return requests.post(url = url, json = content)
#	js = json.dumps(response.json())

#	logging.debug(js)
	
#	if 'error' in js:
#		return Response(js, status = 401, mimetype = 'application/json')
	
#	return Response(js, status = 200, mimetype = 'application/json')

@app.route('/test', methods = ['GET'])
def verify():
	content = {
			'token': '1234',
			'password': 'password'
	}
		
	url = 'http://auth.default.svc.cluster.local/verify'
	
	logging.debug(url)
	
	response = requests.post(url = url, json = content)
	js = json.dumps(response.json())

	logging.debug(js)
	
	if 'error' in js:
		return Response(js, status = 401, mimetype = 'application/json')
	
	return Response(js, status = 200, mimetype = 'application/json')

if __name__ == '__main__':
	app.run(host="0.0.0.0", debug=True)
