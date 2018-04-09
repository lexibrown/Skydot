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
	logging.debug("RESPONSE: " + str(message))
	
	resp = jsonify(message)
	resp.status_code = 400
	return resp
	
@app.errorhandler(500)
def crash(error=None):
	message = {
			'status': 500,
			'error': 'BILL-5000',
			'message': 'Something went wrong. Please try again.'
	}
	logging.debug("RESPONSE: " + str(message))

	resp = jsonify(message)
	resp.status_code = 500
	return resp

@app.route('/bill/payee/search', methods = ['POST'])
def bill_payee_search():
	if not request.json:
		abort(400)
	content = request.get_json()
	
	logging.debug("REQUEST: " + str(content))
	
	if 'search' not in content:
		abort(400)
		
	try:
		url = 'http://host-gateway/host/bill/payee'
		
		response = requests.post(url = url, json = content)
		js = json.dumps(response.json())

		logging.debug("RESPONSE: " + str(js))
		
		if 'error' in js:
			return Response(js, status = 400, mimetype = 'application/json')
		
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)
		
@app.route('/bill/payee', methods = ['POST'])
def bill_payee():
	try:
		url = 'http://host-gateway/host/bill/payee'
		
		response = requests.get(url = url)
		js = json.dumps(response.json())

		logging.debug("RESPONSE: " + str(js))
		
		if 'error' in js:
			return Response(js, status = 400, mimetype = 'application/json')
		
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)
		
if __name__ == '__main__':
	app.run(host="0.0.0.0", port=80, debug=True)
