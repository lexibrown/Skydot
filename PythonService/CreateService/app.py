from flask import Flask, request, abort, jsonify, Response, json
import logging, sys, requests

logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)

app = Flask(__name__)

@app.errorhandler(400)
def invalid(error=None):
	message = {
			'status': 400,
			'error': 'CRT-1000',
			'message': 'Invalid request'
	}
	resp = jsonify(message)
	resp.status_code = 400
	return resp

@app.route('/create', methods = ['POST'])
def create():
	if not request.json:
		abort(400)
	content = request.get_json()

	if 'user_id' not in content:
		abort(400)
		
	if 'password' not in content:
		abort(400)
		
	url = 'https://skydot-bank.azurewebsites.net/host/create'
	
	logging.debug(url)
	
	response = requests.post(url = url, json = content)
	js = json.dumps(response.json())

	logging.debug(js)
	
	if 'error' in js:
		return Response(js, status = 401, mimetype = 'application/json')
	
	return Response(js, status = 200, mimetype = 'application/json')

if __name__ == '__main__':
	app.run(host="0.0.0.0", port=8080, debug=True)
