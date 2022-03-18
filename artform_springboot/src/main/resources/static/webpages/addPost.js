var base_url = "http://localhost:8080/";
var formData = null;

var loadFile = function(event) {
    var output = document.getElementById('img');
    output.src = URL.createObjectURL(event.target.files[0]);
    output.onload = function() {
      URL.revokeObjectURL(output.src);
      formData = new FormData();
      
    }
};

function pubblica() {
  var titolo = document.getElementById("inputTitle").value;
  var post = document.getElementById("inputFile").value;

  if(titolo == "") {
    window.alert("Inserisci un titolo");
    return;
  }
  else if (post == "") {
    window.alert("Seleziona un file");
    return;
  }

  var artist = window.localStorage.getItem("username");
  var postJsonData = '{"artistaUsername":"' + artist + 
                '","titolo":"' + titolo +
                '","topic":"' + "Animali" + // implementare scelta Topic
                '","tags":"' + document.getElementById("inputTags").value + 
                '","dataPubblicazione":"' + /* implementare data corrente */ + 
                '","tipologia":"img","like":"0"}';

  var addPostXmlHttp = new XMLHttpRequest();
  addPostXmlHttp.onreadystatechange = function() {
        if(addPostXmlHttp.readyState == 4) {
            if(addPostXmlHttp.status == 201) {
                window.alert("Pubblicazione avvenuta con successo!");
            }
            else
                window.alert("Si è verificato un problema durante la registrazione");
        }
    };
    addPostXmlHttp.open("POST", base_url + "artform/post");
    addPostXmlHttp.setRequestHeader("Content-Type", "application/json");
    addPostXmlHttp.send(postJsonData);
}