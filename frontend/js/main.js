const url = "http://localhost:8080/login";

function login() {
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    const body = new loginRequest(document.getElementById("username").value, document.getElementById("password").value);

    http_request.onload = function (xhr) {
        if (xhr.target.status === 200) {
            const data = JSON.parse(xhr.target.response);
            setCookie("ROLE", data, 1);
            location.href = "books.html"
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    }
    http_request.open("POST", url, true);
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    http_request.send(JSON.stringify(body))
}

class loginRequest {
    constructor(username, password) {
        this.username = username;
        this.password = password;
    }
}

function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}