var url = "http://localhost:8000/mag/warehouse/myWh";
var url2 = "http://localhost:8000/mag/user/myPerm";

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
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    http_request.send(null);
}

function loadExtraPerms() {
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onreadystatechange = function (xhr) {
        if (xhr.target.status === 200) {
            var list = JSON.parse(xhr.target.response);
            extraPerms(list);
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
    
    http_request.open('GET', url2, true);
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    http_request.send(null);
}

function extraPerms(list) {
    var myList = [];
    var htmlList = [];
    if (list.includes("WAREHOUSE.CREATE.NEW")) {
        myList.push("Create WH");
        htmlList.push("whCreateNew.html");
    }
    if (list.includes("WAREHOUSE.ADD.USER")) {
        myList.push("Add user");
        htmlList.push("whAddUser.html");
    }
    if (list.includes("WAREHOUSE.GET.MEMBER")) {
        myList.push("Check WH workers");
        htmlList.push("whGetMember.html");
    }
    if (list.includes("WAREHOUSE.GET.STORAGE.LOCATION")) {
        myList.push("List of all WHs");
        htmlList.push("whGetStorageLocation.html");
    }
    var button = "";
    for (let i=0; i<myList.length; i++) {
        button = button + " <button class=\"btn btn-warning\" onclick= \"location.href=\'"+htmlList[i]+"\'\">"+myList[i]+"</button>";
    }
    document.getElementById("buttons").innerHTML = button;
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
    location.href = "index.html";
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
