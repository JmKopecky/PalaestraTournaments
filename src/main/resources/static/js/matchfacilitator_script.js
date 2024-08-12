const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/palaestra-websocket'
});


stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);

    stompClient.subscribe('/topic/postfacilitatorconnected', (response) => {
        let status = JSON.parse(response.body).body.status;
        let competitorStatus = JSON.parse(response.body).body.competitorStatus;
        console.log("received response: " + status);
        console.log("received competitor status: " + competitorStatus.body);
        const facilitatorConnectedStatus = document.getElementById("facilitator-status");
        facilitatorConnectedStatus.innerText = status;
        if (status === "Connected") {
            facilitatorConnectedStatus.classList.remove("disconnected");
            facilitatorConnectedStatus.classList.add("connected");
        }
    });

    sendData();
};


stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};


stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


function connect() {
    stompClient.activate();
}


function sendData() {
    stompClient.publish({
        destination: "/app/verifyfacilitatorconnection",
        body: "attempting to connect..."
    });
}


document.addEventListener("DOMContentLoaded", function() {
    connect();
})