from flask import Flask, request, abort, jsonify, Response, json
import logging, sys, requests

logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)

app = Flask(__name__)

@app.errorhandler(400)
def invalid(error=None):
	message = {
			'status': 400,
			'error': 'BILL-1000',
			'message': 'Invalid request'
	}
	resp = jsonify(message)
	resp.status_code = 400
	return resp

@app.route('/bill/payee/search', methods = ['POST'])
def bill_payee_search():
	if not request.json:
		abort(400)
	content = request.get_json()

	if 'search' not in content:
		abort(400)
		
	url = 'https://skydot-bank.azurewebsites.net/host/bill/payee'
	
	logging.debug(url)
	
	response = requests.post(url = url, json = content)
	js = json.dumps(response.json())

	logging.debug(js)
	
	if 'error' in js:
		return Response(js, status = 400, mimetype = 'application/json')
	
	return Response(js, status = 200, mimetype = 'application/json')
	
@app.route('/bill/payee', methods = ['POST'])
def bill_payee():		
	url = 'https://skydot-bank.azurewebsites.net/host/bill/payee'
	
	logging.debug(url)
	
	response = requests.get(url = url)
	js = json.dumps(response.json())

	logging.debug(js)
	
	if 'error' in js:
		return Response(js, status = 400, mimetype = 'application/json')
	
	return Response(js, status = 200, mimetype = 'application/json')

if __name__ == '__main__':
	app.run(host="0.0.0.0", debug=True)
