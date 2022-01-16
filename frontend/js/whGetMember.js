function loadWarehouseMember() {
    if (document.getElementById("whCode").value !== "") {
        let url = "http://localhost:8000/mag/warehouse/code/{code}/members";
        let whCode = document.getElementById("whCode").value;
        let http_request = new XMLHttpRequest();
        http_request.withCredentials = true;
        http_request.onreadystatechange = function (xhr) {
            if (xhr.target.status === 200) {
                var data = JSON.parse(xhr.target.response);
                setTable(data);
                clearError();
            } else if (xhr.target.status === 403) {
                let data = JSON.parse(xhr.target.response);
                if (data.errorCode === "ACCESS_DENIED") {
                    data.errorMessage = "Session expired, login again"
                    handleExceptions(JSON.stringify(data));
                    location.href = "oldunusedindex.html"
                }
                handleExceptions(JSON.stringify(data));
                showError();
            } else {
                handleExceptions(xhr.target.response);
                showError();
            }
        };
        console.log(getCookie('access_token'));
        http_request.open('GET', url.replace("{code}", whCode), true);
        if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
            http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
        } else location.href = "oldunusedindex.html";
        sessionStorage.setItem("lastSearch", whCode);
        http_request.send(null);
    }
}

function removeMember(data) {
    let username = data.parentNode.parentNode.children[0].textContent
    let url = "http://localhost:8000/mag/warehouse/code/{code}/members";
    let whCode = sessionStorage.getItem("lastSearch");
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onreadystatechange = function (xhr) {
        if (xhr.target.status === 200) {
            clearError();
        } else if (xhr.target.status === 403) {
            let data = JSON.parse(xhr.target.response);
            if (data.errorCode === "ACCESS_DENIED") {
                data.errorMessage = "Session expired, login again"
                handleExceptions(JSON.stringify(data));
                location.href = "oldunusedindex.html"
            }
            handleExceptions(JSON.stringify(data));
            showError();
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    };
    console.log(getCookie('access_token'));
    http_request.open('DELETE', url.replace("{code}", whCode) + "?username=" + username, true);
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    } else location.href = "oldunusedindex.html";
    sessionStorage.setItem("lastSearch", whCode);
    http_request.send(null);
}

function setTable(data) {
    document.getElementById("column").innerHTML = "" +
        "<th scope=\"col\">Username</th>\n" +
        "<th scope=\"col\">Permissions</th>\n" +
        "<th scope=\"col\"> </th>\n";

    document.getElementById("body").innerHTML = data.map(function (val) {
        return "" +
            "<tr> " +
            "<th scope=\"row\">" + val.username + "</th>" +
            "<td>" + val.warehousePermissions.map(function (perm) {
                return perm + " ";
            }) + "</td>" +
            "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: red; font-weight: bold' onclick='removeMember(this)'>Remove</i>" + "</td>" +
            "</tr>"
    }).join('') + "";
}

function logout() {
    delete_cookie("access_token");
    location.href = "oldunusedindex.html";
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function delete_cookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function goToWarehouse(element) {
    var p = element.parentNode.parentNode;
    let id = p.firstElementChild.textContent;
    sessionStorage.setItem('code', id);
    location.href = "storageProductList.html";
}

class Warehouse {
    constructor(code, description, address) {
        this.code = code;
        this.description = description;
        this.address = address;
    }
}


class Address {
    constructor(city) {
        this.city = city;
    }
}

// function testowa() {
//     var adres = new Address("Kraków")
//     var magazyn1 = new Warehouse("01","cośtam",adres);
//     var magazyn2 = new Warehouse("02","cośtu",adres);
//     var lista = [magazyn1,magazyn2];
//     setTable(lista);
// }
