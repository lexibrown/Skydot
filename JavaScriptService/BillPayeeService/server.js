'use strict';

const express = require('express'), bodyParser = require('body-parser');
var request = require('request');

const PORT = 8080;
const HOST = '0.0.0.0';

var options = {
    url: "https://skydot-bank.azurewebsites.net/host/bill/payee",
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    }
};

const app = express();
app.use(bodyParser.json());

app.post('/bill/payee/search', function(req, res){
    console.log(req.body);

    var params = req.body;
	
	if (!(params.search)) {
		console.log("invalid params")
		res.status(400).json({
			error: 'BILL-1000',
			message: 'Invalid request'
		});
	} else {
		request.post({
			url: "https://skydot-bank.azurewebsites.net/host/bill/payee",
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

app.post('/bill/payee', function(req, res){
    console.log(req.body);

	request.get({
		url: "https://skydot-bank.azurewebsites.net/host/bill/payee",
		headers: {
			"Content-Type": "application/json"
		},
	}, function(error, response, body){
		if (body.error) {
			res.status(400).json(body);
			return;
		}
		res.setHeader('Content-Type', 'application/json');
		res.send(body);
	});
});

app.listen(PORT, HOST);
console.log('Running on http://${HOST}:${PORT}');
