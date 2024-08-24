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
    let answerContainer = document.getElementById("question-answer-list").querySelector("input");
    let answer;
    if (answerContainer.type === "radio") {
        answer = document.querySelector("input[type = radio]:checked").value;
    } else {
        answer = answerContainer.value;
    }

    console.log(answer);
    console.log(answerContainer);

    stompClient.publish({
        destination: "/app/questionresponse",
        body: sessionStorage.getItem("competitor") + "_" + answer
    });
    console.log("Answer: " + answer + " has been submitted");
}


function skipQuestion() {
    stompClient.publish({
        destination: "/app/questionresponse",
        body: sessionStorage.getItem("competitor") + "_skipped"
    });
    console.log("Skipped question.");
}


const stompClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/palaestra-websocket'
});


stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);

    stompClient.subscribe("/topic/beginmatchinit", (response) => {
        console.log("match began");
    })

    stompClient.subscribe("/topic/receivequestion", (response) => {
        if (JSON.parse(response.body).body["for"] === "facilitator") {
            if (JSON.parse(response.body).body.competitorrequest === true) {
                stompClient.publish({
                    destination: "/app/requestquestiondata",
                    body: sessionStorage.getItem("competitor") + "_false_false"
                })
            }
        } else {
            let data = JSON.parse(response.body).body;
            document.getElementById("waiting-container").style.display = "none";
            document.getElementById("result-container").style.display = "none";
            document.getElementById("question-container").style.display = "block";


            const qnumElem = document.getElementById("question-num");
            qnumElem.innerText = qnumElem.innerText.split("#")[0] + "#" + data["qnum"];

            const qtextElem = document.getElementById("question-text");
            qtextElem.innerText = data["qbody"];

            const questionAnswerContainer = document.getElementById("question-answer-list");
            while (questionAnswerContainer.firstChild) {
                questionAnswerContainer.removeChild(questionAnswerContainer.lastChild);
            }
            if (data["qoptions"][0] === "nomultiplechoice") {
                const inputLabel = document.createElement("label");
                const input = document.createElement("input");input.type = "text";
                inputLabel.textContent = "Answer: ";
                questionAnswerContainer.appendChild(inputLabel);
                questionAnswerContainer.appendChild(input);
            } else {
                for (const answerChoice of data["qoptions"]) {
                    const radioBox = document.createElement("input");radioBox.type = "radio";
                    radioBox.name = "answers"; radioBox.value = answerChoice;radioBox.id = answerChoice;
                    const label = document.createElement("label");
                    label.innerText = answerChoice; label.htmlFor = answerChoice;
                    questionAnswerContainer.appendChild(radioBox);questionAnswerContainer.appendChild(label);
                    questionAnswerContainer.appendChild(document.createElement("br"));
                }
            }
        }
    })

    stompClient.subscribe("/topic/questionanswered", (response) => {
        let data = JSON.parse(response.body).body;
        if (data["competitor"] === sessionStorage["competitor"]) {
            document.getElementById("question-container").style.display = "none";
            document.getElementById("result-container").style.display = "flex";
            if (data["wascorrect"]) {
                document.getElementById("result-text").textContent = "Correct!";
                document.getElementById("result-text").classList.add("correct-text");
                document.getElementById("result-text").classList.remove("incorrect-text");
                document.getElementById("result-container").classList.add("correct-container");
                document.getElementById("result-container").classList.remove("incorrect-container");
            } else if (data["wasskipped"] === true) {
                document.getElementById("result-text").textContent = "Skipped.";
                document.getElementById("result-text").classList.add("incorrect-text");
                document.getElementById("result-text").classList.remove("correct-text");
                document.getElementById("result-container").classList.add("incorrect-container");
                document.getElementById("result-container").classList.remove("correct-container");
            } else {
                document.getElementById("result-text").textContent = "Wrong.";
                document.getElementById("result-text").classList.add("incorrect-text");
                document.getElementById("result-text").classList.remove("correct-text");
                document.getElementById("result-container").classList.add("incorrect-container");
                document.getElementById("result-container").classList.remove("correct-container");

                setTimeout(function() {
                    document.getElementById("question-container").style.display = "block";
                    document.getElementById("result-container").style.display = "none";
                }, 3000);
            }
        } else if (data["lockquestion"]) {
            document.getElementById("question-container").style.display = "none";
            document.getElementById("result-container").style.display = "flex";

            document.getElementById("result-text").textContent = "Question locked by " + data["competitor"];
            document.getElementById("result-text").classList.add("incorrect-text");
            document.getElementById("result-text").classList.remove("correct-text");
            document.getElementById("result-container").classList.add("incorrect-container");
            document.getElementById("result-container").classList.remove("correct-container");
        }
    })

    stompClient.subscribe("/topic/matchscore", (response) => {
        document.getElementById("question-container").style.display = "none";
        document.getElementById("result-container").style.display = "none";
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
            const place = document.createElement("h1");place.textContent = "" + (sorted.length - i);
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
        window.location.replace(window.location.origin);
    });
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