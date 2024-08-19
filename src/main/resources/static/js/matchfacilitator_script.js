const stompClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/palaestra-websocket'
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
        competitorName.innerText += " (" + competitorPasswords[competitorName.innerText] + ")";
        container.appendChild(competitorName);container.appendChild(perCompStatus);
        targetContainer.appendChild(container);
    }
}


function beginMatch() {
    let anyDisconnected = false;
    for (const client of document.getElementById("client-connection-container").children) {
        console.log("running beginMatch checks");
        if (!client.getElementsByTagName("h2")[0].classList.contains("connected")) {
            console.log("some are disconnected");
            anyDisconnected = true;
        }
    }
    if (!anyDisconnected) {
        //proceed and begin match.
        console.log("beginning match");
        stompClient.publish({
            destination: "/app/facilitatorbeginmatch"
        })
    }
}


function nextQuestion() {
    console.log("Proceeding to next question.");
    stompClient.publish({
        destination: "/app/requestquestiondata",
        body: "facilitator_true_true"
    });
}


function endMatch() {
    console.log("Ending match.");
    stompClient.publish({
        destination: "/app/doscorescreen"
    });
}


function returnToControlPanel() {
    console.log("Returning to control panel.");
    stompClient.publish({
        destination: "/app/endmatch"
    });
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

    stompClient.subscribe("/topic/beginmatchinit", (response) => {
        console.log("match began");
        document.getElementById("verify-connections-container").style.display = "none";
        document.getElementById("question-facilitation-container").style.display = "block";
        stompClient.publish({
            destination: "/app/requestquestiondata",
            body: "facilitator_true_false"
        })
    })

    stompClient.subscribe("/topic/receivequestion", (response) => {
        if (JSON.parse(response.body).body["for"] === "facilitator") {
            let data = JSON.parse(response.body).body;
            console.log(data);
            const qnum = document.getElementById("question-num");qnum.textContent = qnum.textContent.split("#")[0] + "#" + data["qnum"];
            const qtext = document.getElementById("question-text");qtext.textContent = data["qbody"];
            const qanswer = document.getElementById("question-answer");qanswer.textContent = qanswer.textContent.split(":")[0] + ": " + data["qanswer"];
            const answerList = document.getElementById("question-alt-answers");answerList.textContent = "Answer List: ";
            for (const option of data["qoptions"]) {
                answerList.textContent += ("" + option + ", ");
            }
            answerList.textContent = answerList.textContent.slice(0, -2);


            const compCont = document.getElementById("competitor-info");
            while (compCont.firstChild) {
                compCont.removeChild(compCont.lastChild);
            }
            for (let i = 0; i < data.competitors.length; i++) {
                const competitor = data.competitors[i].name;
                const competitorContainer = document.createElement("div");competitorContainer.classList.add("competitor-tile");
                const competitorDataWrapper = document.createElement("div");competitorDataWrapper.classList.add("competitor-data-wrapper");
                const competitorName = document.createElement("h3");competitorName.textContent = competitor; competitorName.classList.add("competitor-name");
                competitorContainer.appendChild(competitorName);
                const competitorScore = document.createElement("p");competitorScore.classList.add("competitor-score");
                competitorScore.textContent = "Score: " + data.score[competitor];
                const competitorAttempts = document.createElement("p");competitorAttempts.classList.add("competitor-attempts");
                competitorAttempts.textContent = "Attempts: " + data.attempts[competitor];
                const competitorHasSucceeded = document.createElement("p");competitorHasSucceeded.classList.add("competitor-attempts");
                competitorHasSucceeded.textContent = "Has Answered Correctly: " + data.successes[competitor];
                competitorDataWrapper.appendChild(competitorScore);
                competitorDataWrapper.appendChild(competitorAttempts);
                competitorDataWrapper.appendChild(competitorHasSucceeded);
                competitorContainer.appendChild(competitorDataWrapper);
                compCont.appendChild(competitorContainer);
            }
        }
    })

    stompClient.subscribe("/topic/questionanswered", (response) => {
        stompClient.publish({
            destination: "/app/requestquestiondata",
            body: "facilitator_false_false"
        })
    })

    stompClient.subscribe("/topic/matchscore", (response) => {
        document.getElementById("question-facilitation-container").style.display = "none";
        document.getElementById("match-score-container").style.display = "block";
        let data = JSON.parse(response.body).body;
        let sorted = [];
        for (const competitor in data) {
            sorted.push([competitor, data[competitor]]);
        }
        sorted.sort(function(a, b) {
            return a[1] - b[1];
        });
        for (let i = sorted.length - 1; i >= 0; i--) {
            const placeTile = document.createElement("div");placeTile.classList.add("place-tile");
            const place = document.createElement("h1");place.textContent = "" + (sorted.length - i + 1);
            placeTile.appendChild(place);
            const competitor = document.createElement("h3");competitor.textContent = sorted[i][0];
            placeTile.appendChild(competitor);
            const score = document.createElement("h3");score.textContent = sorted[i][1];
            placeTile.appendChild(score);
            document.getElementById("match-placement-display").appendChild(placeTile);
        }
    });

    stompClient.subscribe("/topic/forceclientsendmatch", (response) => {
        stompClient.deactivate();
        window.location.replace(window.location.origin + "/controlpanel");
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