function populateData(data) {
    const tournamentTile = document.getElementById("tournament-tile");
    while (tournamentTile.firstChild) {
        tournamentTile.removeChild(tournamentTile.lastChild);
    }
    for (let i = 0; i < data["scores"].length; i++) {
        let scoreEntry = data["scores"][i];
        let name = Object.keys(scoreEntry)[0];

        const competitorContainer = document.createElement("div");
        competitorContainer.classList.add("competitor-result-tile");
        const place = document.createElement("h2");
        place.classList.add("place");place.textContent = "#" + (i + 1);
        const competitorName = document.createElement("h3");
        competitorName.classList.add("competitor-name");competitorName.textContent = name;
        const competitorScore = document.createElement("h3");
        competitorScore.classList.add("competitor-score");competitorScore.textContent = scoreEntry[name];

        competitorContainer.appendChild(place);
        competitorContainer.appendChild(competitorName);
        competitorContainer.appendChild(competitorScore);
        tournamentTile.appendChild(competitorContainer);
    }
}


const stompClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/palaestra-websocket'
});


stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);

    stompClient.subscribe("/topic/receivemonitordata", (response) => {
        let data = JSON.parse(response.body).body;
        if (Object.keys(data).length !== 0) {
            populateData(data);
        }
    })

    stompClient.subscribe("/topic/beginmatchinit", (response) => {
        stompClient.publish({
            destination: "/app/requestmonitordata",
            body: "monitor requesting data..."
        })
    })

    stompClient.subscribe("/topic/questionanswered", (response) => {
        stompClient.publish({
            destination: "/app/requestmonitordata",
            body: "monitor requesting data..."
        })
    })

    stompClient.subscribe("/topic/matchscore", (response) => {
        stompClient.publish({
            destination: "/app/requestmonitordata",
            body: "monitor requesting data..."
        })
    })

    stompClient.publish({
        destination: "/app/requestmonitordata",
        body: "monitor connecting..."
    });
    setInterval(function () {
        stompClient.publish({
            destination: "/app/requestmonitordata",
            body: "monitor requesting data..."
        });
    }, 5000);
}


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


document.addEventListener("DOMContentLoaded", function() {
    connect();
})