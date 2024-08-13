function auth() {
    const name = document.getElementById("competitor-name-input").value;
    const pw = document.getElementById("competitor-password-input").value;

    let data = {
        assertedCompetitor: name,
        assertedPassword: pw
    }
    fetch(window.location.href, {
        body: JSON.stringify(data),
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    }).then((response) => {
        return response.text().then((data) => {
            sessionStorage.setItem("competitor", data.substring(data.indexOf("_") + 1, data.lastIndexOf("_")));
            sessionStorage.setItem("password", data.substring(data.lastIndexOf("_")));
            informFacilitator();
            return data;
        }).catch((err) => {
            console.log(err);
        })
    });
}


const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/palaestra-websocket'
});


stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
};


stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};


stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


function informFacilitator() {
    stompClient.publish({
        destination: "/app/verifyfacilitatorconnection",
        body: "Client connected: " + sessionStorage.getItem("competitor")
    });
}


function connect() {
    stompClient.activate();
}


document.addEventListener("DOMContentLoaded", function() {
    connect();
})