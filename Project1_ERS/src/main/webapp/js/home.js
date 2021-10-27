window.onload = function() {
	getSessionUser();
}

function getSessionUser() {
	let xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			let user = JSON.parse(xhttp.responseText);
			console.log(user);
			document.getElementById("welcome-heading").innerHTML = `Welcome ${user.firstname}!`;
		}
	}

	xhttp.open('GET', 'http://localhost:8080/Project1_ERS/getSessionUser.json');

	xhttp.send();
}