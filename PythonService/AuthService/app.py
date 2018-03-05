from flask import Flask, request, abort, jsonify, Response, json
from Crypto.Cipher import XOR
import base64, time, redis

app = Flask(__name__)

cache = redis.Redis(host='redis', port=6379)

delimiter = '\\';
key = "this is the secret key"
import datetime
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
	resp = jsonify(message)
	resp.status_code = 400
	return resp
	
@app.errorhandler(408)
def timeout(error=None):
	message = {
			'status': 408,
			'error': 'AUTH-2000',
			'message': 'Token has expired'
	}
	resp = jsonify(message)
	resp.status_code = 408
	return resp

@app.route('/auth/login', methods = ['POST'])
def login():
	if not request.json:
		abort(400)
	content = request.get_json()

	# TODO call back end to verify account
	
	ts = (time.time())
	st = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')

	logging.debug("ts: " + st)
	token = encrypt(content['user_id'] + delimiter + content['password'] + delimiter + str(ts))
	
	cache.set(content['user_id'], token)
	
	data = {
		'token' : token.decode('utf-8')
	}
	js = json.dumps(data)

	resp = Response(js, status = 200, mimetype = 'application/json')
	return resp

import logging, sys
logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)

@app.route('/auth/verify', methods = ['POST'])
def verify():
	if not request.json:
		abort(400)
	content = request.get_json()

	token = decrypt(content['token']).decode('utf-8').split('\\')
	
	if cache.get(token[0]) is None:
		abort(400)

	then = float(token[2])
	now = time.time()
	elapsed = now - then

	st = datetime.datetime.fromtimestamp(now).strftime('%Y-%m-%d %H:%M:%S')
	logging.debug("Now: " + st)
	st = datetime.datetime.fromtimestamp(then).strftime('%Y-%m-%d %H:%M:%S')
	logging.debug("Then: " + st)
	logging.debug("Diff: " + str(elapsed))
	
	#m, s = divmod(elapsed, 60)
	#h, m = divmod(m, 60)
	m = int(elapsed) / 60
	
	if m > 1:
		abort(408)
	
	data = {
		'message' : m
	}
	js = json.dumps(data)

	resp = Response(js, status = 200, mimetype = 'application/json')
	return resp
	
@app.route('/auth/user', methods = ['POST'])
def user():
	if not request.json:
		abort(400)
	content = request.get_json()
	print (content)
	print (content['token'])

	token = decrypt(content['token'])
	
	data = {
		'user_id' : token.decode('utf-8').split('\\')[0]
	}
	js = json.dumps(data)

	resp = Response(js, status = 200, mimetype = 'application/json')
	return resp
	
if __name__ == '__main__':
	app.run(host="0.0.0.0", debug=True)
