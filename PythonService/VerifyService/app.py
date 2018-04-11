from flask import Flask, request, abort, jsonify, Response, json
import logging, sys, requests, time, datetime

logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)

app = Flask(__name__)

def log(output):
	now = time.time()
	st_now = datetime.datetime.fromtimestamp(now).strftime('%Y-%m-%d %H:%M:%S.%f')
	logging.debug(st_now + " " + output)

@app.errorhandler(400)
def invalid(error=None):
	message = {
			'status': 400,
			'error': 'AUTH-1000',
			'message': 'Invalid request'
	}
	log("[RESPONSE] " + str(message))
	
	resp = jsonify(message)
	resp.status_code = 400
	return resp
	
@app.errorhandler(500)
def crash(error=None):
	message = {
			'status': 500,
			'error': 'AUTH-5000',
			'message': 'Something went wrong. Please try again.'
	}
	log("[RESPONSE] " + str(message))

	resp = jsonify(message)
	resp.status_code = 500
	return resp
	
@app.route('/auth', methods = ['POST'])
def verify():
	if not request.json:
		abort(400)
	content = request.get_json()
	
	log("[REQUEST] " + str(content))
	
	if 'user_id' not in content:
		abort(400)
		
	if 'password' not in content:
		abort(400)
		
	try:
		url = 'http://host-gateway/host/verify'
		
		response = requests.post(url = url, json = content)
		js = json.dumps(response.json())

		log("[RESPONSE] " + str(js))
		
		if 'error' in js:
			return Response(js, status = 401, mimetype = 'application/json')
		
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)
		
if __name__ == '__main__':
	app.run(host="0.0.0.0", port=80, debug=True)
