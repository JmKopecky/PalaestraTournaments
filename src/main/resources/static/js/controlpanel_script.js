document.addEventListener("DOMContentLoaded", function() {


    if (tournament.matchComposer.type === "ffa") {


        for (const match of tournament.matchComposer.matches) {
            const matchContainer = document.createElement("div");
            matchContainer.classList.add("bracket-tile");
            matchContainer.classList.add("tile-ffa");
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
    for (const child of selected.children) {
        newCombatantText += child.innerText + " vs ";
    }
    newCombatantText = newCombatantText.slice(0, -4);
    displaySelectedCombatants.innerText = newCombatantText;
}
