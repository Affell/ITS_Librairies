const { Worker } = require('worker_threads');

function connectSocket(ipAddress, portNumber){
    
    const socket_listener = new Worker("./ServerWorker.js");
    socket_listener.postMessage({type: 'socket', ip: ipAddress, port: portNumber});
    socket_listener.on('message', (msg) => {console.log("serverworker => " + msg)});
}

connectSocket("localhost", 40000);