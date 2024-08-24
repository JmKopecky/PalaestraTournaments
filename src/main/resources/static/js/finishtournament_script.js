document.addEventListener("DOMContentLoaded", function() {
    const resultsContainer = document.getElementById("result-container");
    console.log(results);

    for (let i = 0; i < results["score"].length; i++) {
        let competitorData = results["score"][i];
        let name = Object.keys(competitorData)[0];

        const competitorResultTile = document.createElement("div");
        competitorResultTile.classList.add("competitor-result-tile");
        const place = document.createElement("h2");
        place.classList.add("place");place.textContent = "#" + (i + 1);
        const competitorName = document.createElement("h3");
        competitorName.classList.add("competitor-name");competitorName.textContent = name;
        const competitorScore = document.createElement("h3");
        competitorScore.classList.add("competitor-score");competitorScore.textContent = competitorData[name];

        if (name === results["winner"]) {
            competitorResultTile.classList.add("winner");
        }

        competitorResultTile.appendChild(place);
        competitorResultTile.appendChild(competitorName);
        competitorResultTile.appendChild(competitorScore);
        resultsContainer.appendChild(competitorResultTile);
    }
})