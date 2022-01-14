var url = "http://localhost:8000/mag/myWh";

function loadWarehouseList() {
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
                location.href = "index.html"
            }
            handleExceptions(JSON.stringify(data));
            showError();
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    };
    http_request.open('GET', url, true);
    http_request.send(null);
}

function setTable(data) {
    document.getElementById("column").innerHTML = "" +
        "<th scope=\"col\">Code</th>\n" +
        "<th scope=\"col\">Description</th>\n" +
        "<th scope=\"col\">City</th>\n" +
        "<th scope=\"col\"> </th>\n";

    document.getElementById("body").innerHTML = data.map(function (val) {
        return "" +
            "<tr> " +
            "<th scope=\"row\">" + val.code + "</th>" +
            "<td>" + val.description + "</td>" +
            "<td>" + val.address.city + "</td>" +
            "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: orange; font-weight: bold' onclick='goToWarehouse(this)'>Go to</i>" + "</td>" +
            "</tr>"
    }).join('') + "";
}

function logout() {
    delete_cookie("access_token");
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onload = function (xhr) {
        if (xhr.target.status === 204) {
            clearError();
            console.log("Logout successful")
            location.href = "index.html"
        } else if (xhr.target.status === 403) {
            if (data.errorCode === "ACCESS_DENIED") {
                data.errorMessage = "Session expired, login again"
                handleExceptions(JSON.stringify(data));
            }
            location.href = "index.html"
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    };
    http_request.open('GET', logoutUrl, true);
    http_request.send(null);
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function delete_cookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function goToWarehouse(element){
    var p = element.parentNode.parentNode;
    let id = p.firstElementChild.textContent;

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

function testowa() {
    var adres = new Address("Kraków")
    var magazyn1 = new Warehouse("01","cośtam",adres);
    var magazyn2 = new Warehouse("02","cośtu",adres);
    var lista = [magazyn1,magazyn2];
    setTable(lista);
}
