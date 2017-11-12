var webSocket;


function connect() {
    if (webSocket !== undefined) {
        webSocket.close();
    }

    webSocket = new WebSocket(document.getElementById("address").value);

    webSocket.onmessage = function (event) {
        document.getElementById('response').innerHTML = event.data;
    }
}

function disconnect() {
    webSocket.close();
}

function sendMessage() {
    webSocket.send(document.getElementById("message").value);
}

