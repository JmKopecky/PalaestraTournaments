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
            console.log(data);
            return data;
        }).catch((err) => {
            console.log(err);
        })
    });
}