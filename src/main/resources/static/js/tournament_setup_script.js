
//onClick for the add competitor button
function addCompetitorTile() {
    let currentCountElem = document.getElementById("competitor-count");
    let currentCount = Number(currentCountElem.innerHTML.slice(currentCountElem.innerHTML.lastIndexOf(" ") + 1));
    currentCount++;
    let inputId = "competitor-name-input-" + currentCount;

    const container = document.createElement("div");
    container.classList.add("competitor-tile");
    container.id = "competitor-container-" + currentCount;

    const labelName = document.createElement("label");
    labelName.innerText = "Competitor Name: ";
    const inputName = document.createElement("input");

    inputName.setAttribute("type", "text");
    inputName.id = inputId;
    labelName.setAttribute("for", inputId);

    container.appendChild(labelName);
    container.appendChild(inputName);
    document.getElementById("competitor-list-container").appendChild(container);

    currentCountElem.innerText = "Count: " + currentCount;
}


function submitTournamentData() {
    console.log("Tournament data submit button onclick running...");

    let competitorTiles = document.getElementsByClassName("competitor-tile");
    let competitorNames = [];
    for (let i = 0; i < competitorTiles.length; i++) {
        competitorNames.push(competitorTiles[i].getElementsByTagName("input").item(0).value);
    }

    let defaultMultipleChoice = document.getElementById("select-multiple-choice").checked;
    let defaultQuestionLocking = document.getElementById("select-question-locking").checked;
    let defaultPointsPerCorrect = document.getElementById("select-points-per-correct").valueAsNumber;
    let defaultPointsPerWrong = document.getElementById("select-points-per-wrong").valueAsNumber;
    let defaultPointsPerSkipped = document.getElementById("select-points-per-skipped").valueAsNumber;
    let defaultPointScale = document.getElementById("select-points-scale").valueAsNumber;


    fetch("/tournamentsetup", {
        method: "POST",
        body: JSON.stringify({
            "competitorNames" : competitorNames,
            "defaultMultipleChoice" : defaultMultipleChoice,
            "defaultQuestionLocking" : defaultQuestionLocking,
            "defaultPointsPerCorrect" : defaultPointsPerCorrect,
            "defaultPointsPerWrong" : defaultPointsPerWrong,
            "defaultPointsPerSkipped" : defaultPointsPerSkipped,
            "defaultPointScale" : defaultPointScale,
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    });
}