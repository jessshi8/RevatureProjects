window.onload = function() {
	getAllTickets();
}

function getAllTickets() {
	let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			requests = JSON.parse(xhttp.responseText);
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
				let tdReceipt = tr.appendChild(document.createElement("td"));
				tdReceipt.setAttribute("id", `receipt${i}`);
				let tdAmount = tr.appendChild(document.createElement("td"));
				tdAmount.setAttribute("id", `amount${i}`);
				let tdSubmitted = tr.appendChild(document.createElement("td"));
				tdSubmitted.setAttribute("id", `submitted${i}`);
				let tdAuthor = tr.appendChild(document.createElement("td"));
				tdAuthor.setAttribute("id", `author${i}`);
				let tdResolved = tr.appendChild(document.createElement("td"));
				tdResolved.setAttribute("id", `resolved${i}`);
				let tdResolver = tr.appendChild(document.createElement("td"));
				tdResolver.setAttribute("id", `resolver${i}`);
				let tdStatus = tr.appendChild(document.createElement("td"));
				tdStatus.setAttribute("id", `status${i}`);

				document.getElementById(`request-id${i}`).innerHTML = r.requestid;
				document.getElementById(`type${i}`).innerHTML = r.typeid;
				document.getElementById(`description${i}`).innerHTML = r.description;

				let button = document.getElementById(`receipt${i}`).appendChild(document.createElement("button"));
				let modal = document.getElementById("page").appendChild(document.createElement("div"));
				modal.setAttribute("class", "modal fade");
				modal.setAttribute("id", `receipt-modal${i}`);
				modal.setAttribute("tabindex", "-1");
				modal.setAttribute("aria-labelledby", "label");
				modal.setAttribute("aria-hidden", "true");
				let modalDialog = modal.appendChild(document.createElement("div"));
				modalDialog.setAttribute("class", "modal-dialog");
				let modalContent = modalDialog.appendChild(document.createElement("div"));
				modalContent.setAttribute("class", "modal-content");
				let modalHeader = modalContent.appendChild(document.createElement("div"));
				modalHeader.setAttribute("class", "modal-header");
				let modalTitle = modalHeader.appendChild(document.createElement("h5"));
				modalTitle.setAttribute("class", "modal-title");
				modalTitle.setAttribute("id", "label");
				modalTitle.innerHTML = "Receipt";
				let closeBtn = modalHeader.appendChild(document.createElement("button"));
				closeBtn.setAttribute("type", "button");
				closeBtn.setAttribute("class", "btn-close");
				closeBtn.setAttribute("data-bs-dismiss", "modal");
				closeBtn.setAttribute("aria-label", "Close");
				let modalBody = modalContent.appendChild(document.createElement("div"));
				modalBody.setAttribute("class", "modal-body");
				let image = modalBody.appendChild(document.createElement("img"));
				image.setAttribute("id", `receipt-window${i}`);
				image.setAttribute("class", "img-fluid");
				image.setAttribute("src", `data:image/png;base64,${r.receipt}`);
				button.setAttribute("id", `button${i}`);
				button.setAttribute("data-bs-toggle", "modal");
				button.setAttribute("data-bs-target", `#receipt-modal${i}`);
				button.innerHTML = "View";

				document.getElementById(`amount${i}`).innerHTML = "$" + parseFloat(r.amount).toFixed(2);

				var d = new Date(r.submitted);
				var formattedDate = (d.getMonth() + 1) + "-" + d.getDate() + "-" + d.getFullYear();
				var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();
				var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
				var formattedTime = hours + ":" + minutes;
				document.getElementById(`submitted${i}`).innerHTML = formattedDate + " " + formattedTime;
				document.getElementById(`author${i}`).innerHTML = r.authorId;

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
			getAllDetails();
		}
	}
	xhttp.open('GET', 'http://localhost:8080/Project1_ERS/getTickets.json');
	xhttp.send();
}

function getAllDetails() {
	let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			details = JSON.parse(xhttp.responseText);
			let j = 1;
			for (let i = 0; i < details[0].length; i++) {
				document.getElementById(`type${j}`).innerHTML = details[0][i];
				document.getElementById(`author${j}`).innerHTML = details[1][i];
				if (details[2][i]) {
					document.getElementById(`resolver${j}`).innerHTML = details[2][i];
				} else {
					document.getElementById(`resolver${j}`).innerHTML = "N/A";
				}
				if (details[3][i] == "PENDING") {
					let cell = document.getElementById(`status${j}`);
					cell.innerHTML = "";
					// Approval form
					let form1 = cell.appendChild(document.createElement("form"));
					form1.setAttribute("id", "approve-form");
					form1.setAttribute("method", "POST");
					form1.setAttribute("action", "/Project1_ERS/approve.change");
					let id1 = form1.appendChild(document.createElement("input"));
					id1.setAttribute("type", "hidden");
					id1.setAttribute("name", "id1");
					id1.setAttribute("value", `${document.getElementById(`request-id${j}`).innerHTML}`);
					let button1 = form1.appendChild(document.createElement("button"));
					button1.setAttribute("class", "btn btn-outline-success");
					button1.setAttribute("type", "submit");
					button1.innerHTML = "Approve";
					// Denial form
					let form2 = cell.appendChild(document.createElement("form"));
					form2.setAttribute("id", "denial-form");
					form2.setAttribute("method", "POST");
					form2.setAttribute("action", "/Project1_ERS/deny.change");
					id2 = form2.appendChild(document.createElement("input"));
					id2.setAttribute("type", "hidden");
					id2.setAttribute("name", "id2");
					id2.setAttribute("value", `${document.getElementById(`request-id${j}`).innerHTML}`);
					let button2 = form2.appendChild(document.createElement("button"));
					button2.setAttribute("class", "btn btn-outline-danger");
					button2.setAttribute("type", "submit");
					button2.innerHTML = "Deny";
				} else if (details[3][i] == "APPROVED") {
					let cell = document.getElementById(`status${j}`);
					cell.innerHTML = "";
					cell.setAttribute("style", "background-color: #91bfaa;")
				} else {
					let cell = document.getElementById(`status${j}`);
					cell.innerHTML = "";
					cell.setAttribute("style", "background-color: #edb2b2;")
				}
				j++;
			}
		}
	}
	xhttp.open('GET', 'http://localhost:8080/Project1_ERS/getAllDetails.json');
	xhttp.send();
}
