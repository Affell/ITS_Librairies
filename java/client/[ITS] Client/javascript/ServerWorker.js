const ioc = require("socket.io-client");
const { parentPort } = require('worker_threads');

parentPort.on('message', (message) => {
    if(message.type == 'socket'){
        connectSocket(message.ip, message.port);
    }
});


function connectSocket(ip, port){
    serverSocket = ioc.connect(ip, {port: port});
    serverSocket.on('connect', () => {
        parentPort.postMessage("Connected to " + ip + " at port " + port);
    });
    serverSocket.on('connect_error', function(data){
        parentPort.postMessage('connection error to telnet\n' + data);
    });
    serverSocket.on('connect_timeout', function(data){
        console.log('connection timeout to telnet\n' + data);
    });
}
