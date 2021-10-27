window.onload = function() {
	getSessionTickets();
}

function getSessionTickets() {
	let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			let requests = JSON.parse(xhttp.responseText);
			console.log(requests);
			for (let i = 1; i <= requests.length; i++) {
				let tr = document.getElementById("table-body").appendChild(document.createElement("tr"));
				tr.setAttribute("id", `row${i}`);
			}
			let i = 1;
			for (r of requests) {
				let tr = document.getElementById(`row${i}`);
				let tdRequestId = tr.appendChild(document.createElement("td"));
				tdRequestId.setAttribute("id", `request-id${i}`);
				let tdType = tr.appendChild(document.createElement("td"));
				tdType.setAttribute("id", `type${i}`);
				let tdDescription = tr.appendChild(document.createElement("td"));
				tdDescription.setAttribute("id", `description${i}`);
				let tdAmount = tr.appendChild(document.createElement("td"));
				tdAmount.setAttribute("id", `amount${i}`);
				let tdSubmitted = tr.appendChild(document.createElement("td"));
				tdSubmitted.setAttribute("id", `submitted${i}`);
				let tdResolved = tr.appendChild(document.createElement("td"));
				tdResolved.setAttribute("id", `resolved${i}`);
				let tdResolver = tr.appendChild(document.createElement("td"));
				tdResolver.setAttribute("id", `resolver${i}`);
				let tdStatus = tr.appendChild(document.createElement("td"));
				tdStatus.setAttribute("id", `status${i}`);

				document.getElementById(`request-id${i}`).innerHTML = r.requestid;
				document.getElementById(`type${i}`).innerHTML = r.typeid;
				document.getElementById(`description${i}`).innerHTML = r.description;
				document.getElementById(`amount${i}`).innerHTML = "$" + parseFloat(r.amount).toFixed(2);

				var d = new Date(r.submitted);
				var formattedDate = (d.getMonth() + 1) + "-" + d.getDate() + "-" + d.getFullYear();
				var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
				var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
				var formattedTime = hours + ":" + minutes;
				document.getElementById(`submitted${i}`).innerHTML = formattedDate + " " + formattedTime;

				if (r.resolved != null) {
					var d = new Date(r.resolved);
					var formattedDate = (d.getMonth() + 1) + "-" + d.getDate() + "-" + d.getFullYear();
					var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
					var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
					var formattedTime = hours + ":" + minutes;
					document.getElementById(`resolved${i}`).innerHTML = formattedDate + " " + formattedTime;
				} else {
					document.getElementById(`resolved${i}`).innerHTML = "N/A";
				}
				document.getElementById(`resolver${i}`).innerHTML = r.resolverId;
				document.getElementById(`status${i}`).innerHTML = r.statusid;
				i++;
			}
			getDetails();
		}
	}
	xhttp.open('GET', 'http://localhost:8080/Project1_ERS/getTickets.json');
	xhttp.send();
}

function getDetails() {
	let xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			let details = JSON.parse(xhttp.responseText);
			console.log(details);
			let j = 1;
			for (let i = 0; i < details[0].length; i++) {
				document.getElementById(`type${j}`).innerHTML = details[0][i];
				if (details[1][i]) {
					document.getElementById(`resolver${j}`).innerHTML = details[1][i];
				} else {
					document.getElementById(`resolver${j}`).innerHTML = "N/A";
				}
				document.getElementById(`status${j}`).innerHTML = details[2][i];
				j++;
			}
		}
	}

	xhttp.open('GET', 'http://localhost:8080/Project1_ERS/getDetails.json');

	xhttp.send();
}