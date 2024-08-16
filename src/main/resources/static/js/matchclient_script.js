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
            let data = JSON.parse(response.body).body
            document.getElementById("waiting-container").style.display = "none";
            document.getElementById("question-container").style.display = "block";


            const qnumElem = document.getElementById("question-num");
            qnumElem.innerText = qnumElem.innerText.split("#")[0] + "#" + data["qnum"];

            const qtextElem = document.getElementById("question-text");
            qtextElem.innerText = data["qbody"];

            const questionAnswerContainer = document.getElementById("question-answer-list");
            while (questionAnswerContainer.firstChild) {
                questionAnswerContainer.removeChild(questionAnswerContainer.lastChild);
            }
            for (const answerChoice of data["qoptions"]) {
                const radioBox = document.createElement("input");radioBox.type = "radio";
                radioBox.name = "answers"; radioBox.value = answerChoice;radioBox.id = answerChoice;
                const label = document.createElement("label");
                label.innerText = answerChoice; label.htmlFor = answerChoice;
                questionAnswerContainer.appendChild(radioBox);questionAnswerContainer.appendChild(label);
                questionAnswerContainer.appendChild(document.createElement("br"));
            }
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