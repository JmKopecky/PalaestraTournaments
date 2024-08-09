document.addEventListener("DOMContentLoaded", function() {


    if (tournament.matchComposer.type === "ffa") {

        let pos = 1;
        for (const match of tournament.matchComposer.matches) {
            const matchContainer = document.createElement("div");
            matchContainer.classList.add("bracket-tile");
            matchContainer.classList.add("tile-ffa");

            const matchDifferentiator = document.createElement("h2");
            matchDifferentiator.innerText = "Match " + pos;
            matchContainer.appendChild(matchDifferentiator)

            for (const competitor of match.competitors) {
                const competitorName = document.createElement("h3");
                competitorName.innerText = competitor.name;
                matchContainer.appendChild(competitorName);
            }

            matchContainer.onclick = function() {
                changeSelected(matchContainer);
            }

            const rootContainer = document.getElementById("bracket-container");
            rootContainer.appendChild(matchContainer);
            pos++;
        }
    }


    if (tournament.matchComposer.type === "singleelim") {
        console.log("singleelim");
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
    for (const child of selected.getElementsByTagName("h3")) {
        newCombatantText += child.innerText + " vs ";
    }
    newCombatantText = newCombatantText.slice(0, -4);
    displaySelectedCombatants.innerText = newCombatantText;
}


function updateServerTestData() {
    let selectedMatch = "";
    if (tournament.matchComposer.type === "ffa") {
        selectedMatch += selected.getElementsByTagName("h2")[0].innerText + "_";
    }
    for (const child of selected.getElementsByTagName("h3")) {
        selectedMatch += child.innerText + ",";
    }
    selectedMatch = selectedMatch.slice(0, -1);

    let data = {
        targetMatch: selectedMatch,
        testString: document.getElementById("test").innerText
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

}