const url = "http://localhost:8000/oauth/token";

function login() {
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    const body = new loginRequest(document.getElementById("username").value, document.getElementById("password").value);

    http_request.onload = function (xhr) {
        if (xhr.target.status === 200) {
            const data = JSON.parse(xhr.target.response);
            setCookie("access_token", data, 1);
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    }
    http_request.open("POST", url, true);
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    http_request.send(JSON.stringify(body));
}

function register(){
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    var body;
    http_request.onload = function (xhr) {
        if (xhr.target.status === 204) {
            location.href = "index.html";
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    }
    if(document.getElementById("username").value!="" && document.getElementById("password").value!="" && document.getElementById("mobile").value!="" && document.getElementById("email").value!="") {
        const address = new address(document.getElementById("mobile").value, document.getElementById("email").value);
        body = new registrationForm(document.getElementById("username").value, document.getElementById("password").value, address);
        http_request.open("POST", url, true);
        http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        http_request.send(JSON.stringify(body))
    } else {
        handleExceptions("{\"errorMessage\":\"Empty Field/s\"}");
        showError();
    }
}
class address {
    constructor(mobile, mail) {
        this.mobile = mobile;
        this.mail = mail;
    }
}
class registrationForm {
    constructor(username, password, address) {
        this.username = username;
        this.password = password;
        this.address = address;
    }
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