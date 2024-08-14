const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/palaestra-websocket'
});


function processCompetitorStatus(competitorStatus, competitorPasswords) {
    const targetContainer = document.getElementById("client-connection-container");
    while (targetContainer.firstChild) {
        targetContainer.removeChild(targetContainer.lastChild);
    }
    for (const competitor in competitorStatus) {
        const container = document.createElement("div");container.classList.add("client-connection-tile");
        const competitorName = document.createElement("h1");competitorName.innerText = competitor;
        const perCompStatus = document.createElement("h2");perCompStatus.innerText = competitorStatus[competitor];
        if (perCompStatus.innerText === "Connected") {
            perCompStatus.classList.add("connected");
        } else if (perCompStatus.innerText === "Disconnected") {
            perCompStatus.classList.add("disconnected");
        }
        container.appendChild(competitorName);container.appendChild(perCompStatus);
        targetContainer.appendChild(container);
    }

    console.log(competitorPasswords);
}


stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);

    stompClient.subscribe('/topic/postfacilitatorconnected', (response) => {
        let status = JSON.parse(response.body).body.status;
        let competitorStatus = JSON.parse(response.body).body.competitorStatus;
        let competitorPasswords = JSON.parse(response.body).body.competitorPasswords;
        console.log("received response: " + status);
        const facilitatorConnectedStatus = document.getElementById("facilitator-status");
        facilitatorConnectedStatus.innerText = status;
        if (status === "Connected") {
            facilitatorConnectedStatus.classList.remove("disconnected");
            facilitatorConnectedStatus.classList.add("connected");
        }

        processCompetitorStatus(competitorStatus, competitorPasswords);
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