document.addEventListener("DOMContentLoaded", function() {


    if (tournament.matchComposer.type === "ffa") {

        let pos = 1;
        for (const match of tournament.matchComposer.matches) {
            let completedMatch = match["concluded"];
            const matchContainer = document.createElement("div");
            matchContainer.classList.add("bracket-tile");
            matchContainer.classList.add("tile-ffa");
            if (completedMatch === true) {
                matchContainer.classList.add("finished-match");
            }

            const matchDifferentiator = document.createElement("h3");
            matchDifferentiator.innerText = "Match " + pos;
            matchContainer.appendChild(matchDifferentiator)

            for (const competitor of match.competitors) {
                const competitorName = document.createElement("h4");
                competitorName.innerText = competitor.name;
                matchContainer.appendChild(competitorName);
            }

            matchContainer.onclick = function() {
                changeSelected(matchContainer);
                selectedData = match;
            }

            const rootContainer = document.getElementById("bracket-container");
            rootContainer.appendChild(matchContainer);
            pos++;
        }
    }


    if (tournament.matchComposer.type === "singleelim") {
        console.log("singleelim");
        let pos = 1;
        for (const match of tournament.matchComposer.matches) {

            if (match["isPlaceholder"] === true) {
                continue;
            }
            let completedMatch = match["concluded"];
            const matchContainer = document.createElement("div");
            matchContainer.classList.add("bracket-tile");
            matchContainer.classList.add("tile-single-elim");
            if (completedMatch === true) {
                matchContainer.classList.add("finished-match");
            }

            const matchTitle = document.createElement("h3");
            matchTitle.textContent = "Match " + pos;
            matchContainer.appendChild(matchTitle);

            for (const competitor of match.competitors) {
                const competitorName = document.createElement("h4");
                competitorName.innerText = competitor.name;
                matchContainer.appendChild(competitorName);
            }

            matchContainer.onclick = function() {
                changeSelected(matchContainer);
                selectedData = match;
            }

            const rootContainer = document.getElementById("bracket-container");
            rootContainer.appendChild(matchContainer);
            pos++;
        }
    }
})


function changeSelected(newSelected) {
    if (selected != null) {
        selected.classList.remove("selected-match");
    }
    selected = newSelected;
    selected.classList.add("selected-match");

    //change the identifying text in config.
    let displaySelectedCombatants = document.getElementById("combatants");
    let newCombatantText = "";
    for (const child of selected.getElementsByTagName("h4")) {
        newCombatantText += child.innerText + " vs ";
    }
    newCombatantText = newCombatantText.slice(0, -4);
    displaySelectedCombatants.innerText = newCombatantText;
}


function showScore() {
    console.log("showing score...");
    if (selectedData !== undefined) {
        document.getElementById("match-score-container").style.display = "block";
        document.getElementById("bracket-container").style.display = "none";
        document.getElementById("match-config-container").style.display = "none";
        let data = selectedData["matchScore"];
        let sorted = [];
        for (const competitor in data) {
            sorted.push([competitor, data[competitor]]);
        }
        sorted.sort(function(a, b) {
            return a[1] - b[1];
        });
        const container = document.getElementById("match-placement-display");
        while (container.firstChild) {
            container.removeChild(container.lastChild);
        }
        for (let i = sorted.length - 1; i >= 0; i--) {
            const placeTile = document.createElement("div");placeTile.classList.add("place-tile");
            const place = document.createElement("h1");place.textContent = "" + (sorted.length - i);
            placeTile.appendChild(place);
            const competitor = document.createElement("h3");competitor.textContent = sorted[i][0];
            placeTile.appendChild(competitor);
            const score = document.createElement("h3");score.textContent = sorted[i][1];
            placeTile.appendChild(score);
            container.appendChild(placeTile);
        }
    }
}


function hideScore() {
    document.getElementById("match-score-container").style.display = "none";
    document.getElementById("bracket-container").style.display = "block";
    document.getElementById("match-config-container").style.display = "block";
}


function endTournament() {
    console.log("Tournament ended.");
}


function updateServerTestData() {
    let selectedMatch = "";
    if (tournament.matchComposer.type === "ffa") {
        selectedMatch += selected.getElementsByTagName("h3")[0].innerText + "_";
    }

    for (const child of selected.getElementsByTagName("h4")) {
        selectedMatch += child.innerText + ",";
    }
    selectedMatch = selectedMatch.slice(0, -1);

    let data = {
        targetMatch: selectedMatch,
        testString: document.getElementById("test").value
    }
    fetch(window.location.href, {
        body: JSON.stringify(data),
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    }).then((response) => {
        return response.json().then((data) => {
            populateQuestionTiles(data);
            return data;
        }).catch((err) => {
            console.log(err);
        })
    });
}


function populateQuestionTiles(data) {

    const toRemove = document.getElementById("question-container");
    while (toRemove.firstChild) {
        toRemove.removeChild(toRemove.lastChild);
    }

    for (const question of data) {
        const questionTile = document.createElement("div");
        questionTile.classList.add("question-tile");

        const bodyText = document.createElement("p");
        bodyText.innerText = question.questionBody;
        bodyText.classList.add("question-text");
        questionTile.appendChild(bodyText);

        const answerContainer = document.createElement("div");
        answerContainer.classList.add("answer-container");

        const answerLabel = document.createElement("label");
        answerLabel.innerText = "Answer: ";
        const answerText = document.createElement("input");
        answerText.value = question.answer;
        answerText.classList.add("answer-input");
        answerContainer.appendChild(answerLabel);
        answerContainer.appendChild(answerText);

        answerContainer.appendChild(document.createElement("br"));

        const answerChoicesLabel = document.createElement("label");
        answerChoicesLabel.innerText = "Wrong Answer Choices: ";
        const answerChoicesText = document.createElement("input");
        let answerChoiceTextValue = "";
        for (const falseAnswer of question.alternateAnswers) {
            answerChoiceTextValue += falseAnswer + ", ";
        }
        answerChoiceTextValue = answerChoiceTextValue.slice(0, -1);
        answerChoicesText.value = answerChoiceTextValue;
        answerContainer.appendChild(answerChoicesLabel);
        answerContainer.appendChild(answerChoicesText);

        questionTile.appendChild(answerContainer);

        const fixFloat = document.createElement("div");
        fixFloat.setAttribute("style", "clear: both");
        questionTile.appendChild(fixFloat);

        document.getElementById("question-container").appendChild(questionTile);
    }
}