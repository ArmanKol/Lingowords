function getWords(){
    fetch('/api/words')
        .then(response => response.json().then(responseData => {
            for(const data of responseData){
				var tBody = document.createElement("tbody");

				var row = document.createElement("tr");
                
                var columnWords = document.createElement("td");
                var wordsText = document.createTextNode(data.word);
                columnWords.appendChild(wordsText);
                row.appendChild(columnWords);

				tBody.appendChild(row);
                document.querySelector("#wordsTable").appendChild(tBody);
			}
        }));
}

getWords();