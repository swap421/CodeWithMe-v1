document.addEventListener("DOMContentLoaded", function() {
    // navbar js
    const image = document.getElementById('burger');
    const targetElement = document.getElementById('navbarNav');

    image.addEventListener('click', () => {
        targetElement.classList.toggle('show-nav');
    });
    const message = document.getElementById('enable-chatbox');
    const messageContainer = document.getElementById("details-container");
    message.addEventListener('click', () => {
        messageContainer.classList.toggle('show-chatbox');
        if (messageContainer.classList.contains('show-chatbox')) {
            message.textContent = 'Show Ratings';
        } else {
            message.textContent = 'Show Messages';
        }
    });
    var isEditForm = document.getElementById("edit-profile");
    if (isEditForm){
        var uploadForm = document.getElementById("edit-profile");
        var fileInput = document.getElementById("profileImage");
        uploadForm.addEventListener("submit", function(event) {
            var username = document.getElementById("username").value;
            if(username.length < 1 || username.length > 25){
                event.preventDefault();
                alert("Username must be between 1 to 25 characters");
            }
            var bio = document.getElementById("bio").value;
            if(bio.length < 5 || bio.length > 75){
                event.preventDefault();
                alert("Bio must be between 5 to 75 characters");
            }
            if(fileInput.files[0]){
                var fileSize = fileInput.files[0].size;
                var maxSize = 3 * 1024 * 1024; // 1 MB (convert to bytes)

                if (fileSize > maxSize) {
                    event.preventDefault();
                    alert("Choose file smaller than < 3mb");
                }
            }
            var lcu = document.getElementById("leetcodeUsername").value;
            if(lcu.length > 30){
                event.preventDefault();
                alert("LC username must be less than 30 characters");
            }
            var cfu = document.getElementById("codeforceUsername").value;
            if(cfu.length > 30){
                event.preventDefault();
                alert("CF username must be less than 30 characters");
            }
        });
    }
    var isFindPartner = document.getElementById("find-partner");
    if (isFindPartner){
        // ajax request to send connection request
        var sendRequestDivs = document.querySelectorAll(".send-request");
        sendRequestDivs.forEach(function(sendRequestDiv) {
            var sendRequestButton = sendRequestDiv.querySelector("button");
            var greenTick = sendRequestDiv.querySelector("img");
            sendRequestButton.addEventListener("click", function() {
                var userId = this.getAttribute("data-user-id");
                var requestData = {"data-user-id": userId};
                var xhr = new XMLHttpRequest();
                xhr.open("POST", "/send-request", true);
                xhr.setRequestHeader("Content-Type", "application/json");
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status === 200) {
                            console.log(xhr.responseText);
                            sendRequestButton.classList.add("request-sent");
                            sendRequestButton.classList.add("hide");
                            greenTick.classList.remove("hide");
                        } else {
                            console.error(xhr.responseText);
                        }
                    }
                };
                xhr.send(JSON.stringify(requestData));
            });
        });
    }
    var isPendingRequest = document.getElementById("pending-request");
        if (isPendingRequest){
            // ajax request to accept connection request
            var AcceptRequestDivs = document.querySelectorAll(".accept-request");
            AcceptRequestDivs.forEach(function(AcceptRequestDiv) {
                var acceptRequestButton = AcceptRequestDiv.querySelector("button");
                var greenTick = AcceptRequestDiv.querySelector("img");
                acceptRequestButton.addEventListener("click", function() {
                    var userId = this.getAttribute("data-user-id");
                    var requestData = {"data-user-id": userId};
                    var xhr = new XMLHttpRequest();
                    xhr.open("POST", "/accept-request", true);
                    xhr.setRequestHeader("Content-Type", "application/json");
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState === XMLHttpRequest.DONE) {
                            if (xhr.status === 200) {
                                console.log(xhr.responseText);
                                acceptRequestButton.classList.add("request-accepted");
                                acceptRequestButton.classList.add("hide");
                                greenTick.classList.remove("hide");
                            } else {
                                console.error(xhr.responseText);
                            }
                        }
                    };
                    xhr.send(JSON.stringify(requestData));
                });
            });
        }
});




