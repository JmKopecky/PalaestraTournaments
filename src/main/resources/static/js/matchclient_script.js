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
    let answer = document.querySelector("input[type = radio]:checked").value;
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
    brokerURL: 'ws://localhost:8080/palaestra-websocket'
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
                    body: sessionStorage.getItem("competitor") + "_false"
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