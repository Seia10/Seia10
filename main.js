var xmlHttpRequest;

function sendSendRequest() {
	var messageElement = document.getElementById("message");
	var fileElement = document.getElementById("file_upload");
/*	var url = "send";
	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkSendRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send("message=" + encodeURIComponent(messageElement.value) + "&file_upload=" + fileElement.files[0]);
*/
	var formData = new FormData();

	formData.append("message", messageElement.value);

	

	// ファイルが存在する場合に FormData に情報を追加
    if (fileElement && fileElement.files.length > 0) {
        formData.append("file_upload", fileElement.files[0]);
    }else{
		formData.append("file_upload", null);
	}

	var url = "send";
	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkSendRequest;
	xmlHttpRequest.open("POST", url, true);
	xmlHttpRequest.send(formData);
}

function receiveSendResponse() {
	//var response = eval("(" + xmlHttpRequest.responseText + ")");
}

function checkSendRequest() {
	if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
		receiveSendResponse();
	}
}




function sendUpdateRequest() {
	var url = "update";
	
	xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.onreadystatechange = checkUpdateRequest;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.send(null);
}

function receiveUpdateResponse() {
	//console.log(xmlHttpRequest.responseText)
    var response = JSON.parse(xmlHttpRequest.responseText);
    var nameElement = document.getElementById("name");
    nameElement.innerHTML = response.user.name;
    var statementListElement = document.getElementById("statement_list");
    statementListElement.innerHTML = "";

    for (var i = 0; i < response.statementList.length; i++) {
        var statement = response.statementList[i];
        var statementElement = document.createElement("tr");
        statementListElement.appendChild(statementElement);

        var userElement = document.createElement("td");
        statementElement.appendChild(userElement);
        userElement.innerHTML = statement.user.name + ":";

        var messageElement = document.createElement("td");
        statementElement.appendChild(messageElement);
        messageElement.innerHTML = statement.message;

        // 画像がある場合は img タグを生成して表示
		if (statement.img !== 'null') {
			var imgElement = document.createElement("img");
			imgElement.src = "data:image/png;base64," + statement.img;
			imgElement.alt = "Image";
			// 画像の最大幅を設定
			imgElement.style.maxWidth = "500px";
			// 画像を表示する要素に追加
			statementElement.appendChild(imgElement);
		}

    }
}


function checkUpdateRequest() {
	if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
		receiveUpdateResponse();
	}
}

window.addEventListener("load", function() {
	var sendButtonElement = document.getElementById("send_button");
	sendButtonElement.addEventListener("click", sendSendRequest, false);
	sendUpdateRequest();
	setInterval(sendUpdateRequest, 1000);
}, false);

