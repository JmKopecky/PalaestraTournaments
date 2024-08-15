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
            informFacilitatorConnected();
            document.getElementById("auth-container").style.display = "none";
            document.getElementById("waiting-container").style.display = "flex";
            return data;
        }).catch((err) => {
            console.log(err);
        })
    });
}


function submitAnswer() {
    console.log("submitting...");
}


function skipQuestion() {
    console.log("skipping...");
}


const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/palaestra-websocket'
});


stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);

    stompClient.subscribe("/topic/beginmatchinit", (response) => {
        console.log("match began");
    })

    stompClient.subscribe("/topic/receivequestion", (response) => {
        if (JSON.parse(response.body).body.for === "facilitator") {
            stompClient.publish({
                destination: "/app/requestquestiondata",
                body: sessionStorage.getItem("competitor")
            })
        } else {
            console.log(JSON.parse(response.body).body);
            document.getElementById("waiting-container").style.display = "none";
            document.getElementById("question-container").style.display = "block";
        }
    })
};


stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};


stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


function informFacilitatorConnected() {
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