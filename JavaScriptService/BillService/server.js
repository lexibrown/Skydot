'use strict';

const express = require('express'), bodyParser = require('body-parser');
var request = require('request');

const PORT = 80;
const HOST = '0.0.0.0';

var options = {
    url: "http://host-gateway/host/bill",
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    }
};

var invalid = {
	error: 'BILL-1000',
	message: 'Invalid request'
};

var crash = {
	error: 'BILL-5000',
	message: 'Something went wrong. Please try again.'
};

const app = express();
app.use(bodyParser.json());

var moment = require('moment'),
timestamp = moment().format('YYYY-MM-DD HH:mm:ss.SSS');

var morgan = require('morgan')
morgan.token("date-time", function (req, res) { return timestamp });
app.use(morgan(':date-time [:method] :url :status - :response-time ms'))

function handleError(res, error) {
	console.log ('error', error.message, error.stack)
	console.log("[RESPONSE] ", crash);
	res.status(500).json(crash);
};

app.post('/bill', function(req, res){
    try {
		console.log("[REQUEST] ", req.body);

		var params = req.body;
		
		if (!(params.user_id) || !(params.from_account) || !(params.payee) || !(params.amount) || !(params.currency)) {
			console.log("invalid params")
			console.log("[RESPONSE] ", invalid);
			res.status(400).json(invalid);
		} else {
			request.post({
				url: "http://host-gateway/host/bill",
				headers: {
					"Content-Type": "application/json"
				},
				body: params,
				json:true
			}, function(error, response, body){
				if (error) {
					handleError(res, error);
					return;
				}
				console.log("[RESPONSE] ", body);
				if (body.error) {
					res.status(400).json(body);
					return;
				}
				res.send(body);
			});
		}
	} catch (error) {
		handleError(res, error);
	}
});

app.listen(PORT, HOST);
console.log('Running on http://${HOST}:${PORT}');
















