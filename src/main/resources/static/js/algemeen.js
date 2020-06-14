function addWord(){
    var input = document.getElementById("inputWord").value;

    fetch('api/words/add/', {method: 'POST', body: input})
        .then(response => response.json().then(data => {
            console.log(data.message);
        }));
}

function deleteWord(){
    var input = document.getElementById("inputWord").value;

    fetch('api/words/delete/', {method: 'DELETE', body: input})
        .then(response => response.json().then(data => {
            console.log(data.message);
        }));
        
}
