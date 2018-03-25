'use strict';

const express = require('express');
var http = require("http");

// Constants
const PORT = 8080;
const HOST = '0.0.0.0';

var options = {
    host: "https://skydot-bank.azurewebsites.net",
    path: "/host/<insert>",
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    }
};

// App
const app = express();
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

app.listen(PORT, HOST);
console.log('Running on http://${HOST}:${PORT}');