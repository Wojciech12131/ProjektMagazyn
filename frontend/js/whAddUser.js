function addUserToWarehouse() {
    let url = "http://localhost:8000/mag/warehouse/code/{code}/addUser";
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onload = function (xhr) {
        if (xhr.target.status === 204) {
            location.href = "warehouseList.html";
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    }

    let whCode = document.getElementById("codeWh").value;
    let username = document.getElementById("username").value;
    let permissions = [];
    if (document.getElementById("checkbox1").checked === true)
        permissions.push("REMOVE.MEMBER")
    if (document.getElementById("checkbox2").checked === true)
        permissions.push("ADD.MEMBER")
    if (document.getElementById("checkbox3").checked === true)
        permissions.push("MODIFY_SHELVES")
    if (document.getElementById("checkbox4").checked === true)
        permissions.push("MODIFY_WAREHOUSE")
    let body = new Body(username, permissions)
    http_request.open("POST", url.replace("{code}", whCode), true);
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    } else location.href = "oldunusedindex.html";

    http_request.send(JSON.stringify(body))
}

class Body {
    username;
    warehousePermissions;

    constructor(username, warehousePermissions) {
        this.username = username;
        this.warehousePermissions = warehousePermissions;
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

// username
// codeWh