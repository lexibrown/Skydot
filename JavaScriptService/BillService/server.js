'use strict';

const express = require('express'), bodyParser = require('body-parser');
var request = require('request');

const PORT = 8080;
const HOST = '0.0.0.0';

var options = {
    url: "https://skydot-bank.azurewebsites.net/host/bill",
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    }
};

const app = express();
app.use(bodyParser.json());

app.post('/bill', function(req, res){
    console.log(req.body);

    var params = req.body;
	
	if (!(params.user_id) || !(params.from_account) || !(params.payee) || !(params.amount) || !(params.currency)) {
		console.log("invalid params")
		res.status(400).json({
			error: 'BILL-1000',
			message: 'Invalid request'
		});
	} else {
		request.post({
			url: "https://skydot-bank.azurewebsites.net/host/bill",
			headers: {
				"Content-Type": "application/json"
			},
			body: params,
			json:true
		}, function(error, response, body){
			if (body.error) {
				res.status(400).json(body);
				return;
			}
			res.send(body);
		});
	}	
});

app.listen(PORT, HOST);
console.log('Running on http://${HOST}:${PORT}');
















