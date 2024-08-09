
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


    document.getElementById("competitor-bottombar").setAttribute("style", "visibility: visible");
    for (let i = 1; i < 8; i++) {
        let requiredCount = 2 ** i;
        if (currentCount === requiredCount) {
            //single elimination tournament available
            document.getElementById("single-elim-label").setAttribute("style", "visibility: visible");
            document.getElementById("single-elim-radio").setAttribute("style", "visibility: visible");
            break;
        } else {
            //single elimination tournament not available
            document.getElementById("single-elim-label").setAttribute("style", "visibility: hidden");
            document.getElementById("single-elim-radio").setAttribute("style", "visibility: hidden");
            document.getElementById("ffa-radio").checked = true;
            selectTournamentType();
        }
    }
}


function selectTournamentType() {
    if (document.getElementById("ffa-radio").checked) {
        document.getElementById("matches-count").setAttribute("style", "visibility: visible");
        document.getElementById("matches_count_label").setAttribute("style", "visibility: visible");
    } else {
        document.getElementById("matches-count").setAttribute("style", "visibility: hidden");
        document.getElementById("matches_count_label").setAttribute("style", "visibility: hidden");
    }
}


function submitTournamentData() {
    console.log("Tournament data submit button onclick running...");

    let competitorTiles = document.getElementsByClassName("competitor-tile");
    let competitorNames = [];
    for (let i = 0; i < competitorTiles.length; i++) {
        competitorNames.push(competitorTiles[i].getElementsByTagName("input").item(0).value);
    }

    let tournamentType;
    if (document.getElementById("ffa-radio").checked) {
        tournamentType = "ffa_" + document.getElementById("matches-count").valueAsNumber;
    }
    if (document.getElementById("single-elim-radio").checked) {
        tournamentType = "singleelim";
    }

    //submit form
    fetch("/tournamentsetup", {
        method: "POST",
        body: JSON.stringify({
            "competitorNames": competitorNames,
            "defaultMultipleChoice": document.getElementById("select-multiple-choice").checked,
            "defaultQuestionLocking": document.getElementById("select-question-locking").checked,
            "defaultPointsPerCorrect": document.getElementById("select-points-per-correct").valueAsNumber,
            "defaultPointsPerWrong": document.getElementById("select-points-per-wrong").valueAsNumber,
            "defaultPointsPerSkipped": document.getElementById("select-points-per-skipped").valueAsNumber,
            "defaultPointScale": document.getElementById("select-points-scale").valueAsNumber,
            "tournamenttype": tournamentType
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    }).then(r => window.location.replace(window.location.origin + "/controlpanel"));
}