from flask import Flask, request, abort, jsonify, Response, json
from Crypto.Cipher import XOR
import base64, time, redis, datetime
import logging, sys, requests

logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)

app = Flask(__name__)

cache = redis.Redis(host='redis', port=6379)

delimiter = '\\';
key = "this is the secret key"

def encrypt(plaintext):
	cipher = XOR.new(key)
	return base64.b64encode(cipher.encrypt(plaintext))

def decrypt(ciphertext):
	cipher = XOR.new(key)
	return cipher.decrypt(base64.b64decode(ciphertext))

@app.errorhandler(400)
def invalid(error=None):
	message = {
			'status': 400,
			'error': 'AUTH-1000',
			'message': 'Invalid request'
	}
	logging.debug("RESPONSE: " + str(message))
	
	resp = jsonify(message)
	resp.status_code = 400
	return resp
	
@app.errorhandler(401)
def unauthorized(error=None):
	message = {
			'status': 401,
			'error': 'AUTH-1001',
			'message': 'Token is invalid',
			'token': 'INVALID'
	}
	logging.debug("RESPONSE: " + str(message))
	
	resp = jsonify(message)
	resp.status_code = 401
	return resp

@app.errorhandler(408)
def timeout(error=None):
	message = {
			'status': 408,
			'error': 'AUTH-1008',
			'message': 'Token has expired',
			'token': 'EXPIRED'
	}
	logging.debug("RESPONSE: " + str(message))
	
	resp = jsonify(message)
	resp.status_code = 408
	return resp

@app.errorhandler(500)
def crash(error=None):
	message = {
			'status': 500,
			'error': 'AUTH-5000',
			'message': 'Something went wrong. Please try again.'
	}
	logging.debug("RESPONSE: " + str(message))

	resp = jsonify(message)
	resp.status_code = 500
	return resp

@app.route('/auth/login', methods = ['POST'])
def login():
	if not request.json:
		abort(400)
	content = request.get_json()

	logging.debug("REQUEST: " + str(content))
	
	if 'user_id' not in content:
		abort(400)
		
	if 'password' not in content:
		abort(400)
	
	try :
		url = 'http://verify-service-python/auth'
		#url = 'http://verify-service-cplus/auth'
		
		response = requests.post(url = url, json = content)
		js = json.dumps(response.json())

		logging.debug("VERIFY RESPONSE: " + str(js))
		
		if 'error' in js:
			return Response(js, status = 401, mimetype = 'application/json')
		
		if 'success' not in js:
			return Response(js, status = 401, mimetype = 'application/json')	
		
		ts = (time.time())
		st = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')

		logging.debug("ts: " + st)
		token = encrypt(content['user_id'] + delimiter + content['password'] + delimiter + str(ts))
		
		cache.set(content['user_id'], token)
		
		data = {
			'token' : token.decode('utf-8')
		}
		js = json.dumps(data)
			
		logging.debug("RESPONSE: " + str(js))
			
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)
		
@app.route('/auth/logout', methods = ['POST'])
def logout():
	if not request.json:
		abort(400)
	content = request.get_json()

	logging.debug("REQUEST: " + str(content))
	
	if 'token' not in content:
		abort(400)
	
	try:
		token = decrypt(content['token']).decode('utf-8').split('\\')
	except:
		abort(500)
	
	if cache.get(token[0]) is None:
		abort(401)
	
	try:
		cache.delete(token[0])
		
		data = {
			'message' : 'Logged out'
		}
		js = json.dumps(data)
		
		logging.debug("RESPONSE: " + str(js))
			
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)
		
@app.route('/auth/verify', methods = ['POST'])
def verify():
	if not request.json:
		abort(400)
	content = request.get_json()

	logging.debug("REQUEST: " + str(content))
	
	if 'token' not in content:
		abort(400)
	
	try:
		sent_token = decrypt(content['token']).decode('utf-8').split('\\')
	except:
		abort(500)
	
	if cache.get(sent_token[0]) is None:
		abort(401)

	try:
		token = decrypt(cache.get(sent_token[0])).decode('utf-8').split('\\')
			
		then = float(token[2])
		now = time.time()
		elapsed = now - then

		st_now = datetime.datetime.fromtimestamp(now).strftime('%Y-%m-%d %H:%M:%S')
		logging.debug("Now: " + st_now)
		st_then = datetime.datetime.fromtimestamp(then).strftime('%Y-%m-%d %H:%M:%S')
		logging.debug("Then: " + st_then)
		logging.debug("Diff: " + str(elapsed))

		m = int(elapsed) / 60
	except:
		abort(500)
		
	if m > 10:
		abort(408)

	try:
		new_token = encrypt(token[0] + delimiter + token[1] + delimiter + str(now))
		
		cache.set(token[0], new_token)
		
		data = {
			'success' : 'Valid token'
		}
		js = json.dumps(data)
		
		logging.debug("RESPONSE: " + str(js))
		
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)

@app.route('/auth/user', methods = ['POST'])
def user():
	if not request.json:
		abort(400)
	content = request.get_json()

	logging.debug("REQUEST: " + str(content))
	
	if 'token' not in content:
		abort(400)
	
	try:
		token = decrypt(content['token'])
		
		data = {
			'user_id' : token.decode('utf-8').split('\\')[0]
		}
		js = json.dumps(data)

		logging.debug("RESPONSE: " + str(js))
		
		return Response(js, status = 200, mimetype = 'application/json')
	except:
		abort(500)

if __name__ == '__main__':
	app.run(host="0.0.0.0", port=80, debug=True)
