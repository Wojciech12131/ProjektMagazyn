var url = "http://localhost:8000/mag/warehouse/productList";
var url2 = "http://localhost:8000/mag/warehouse/createOrder";

function loadAllProductList() {
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onreadystatechange = function (xhr) {
        if (xhr.target.status === 200) {
            var data = JSON.parse(xhr.target.response);
            setTableAllProducts(data);
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
    console.log(getCookie('access_token'));
    http_request.open('GET', url, true);
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    http_request.send(null);
}


function setTableAllProducts(data) {
    document.getElementById("column").innerHTML = "" +
        "<th scope=\"col\">Code</th>\n" +
        "<th scope=\"col\">Description</th>\n" +
        "<th scope=\"col\">Quantity</th>\n" +
        "<th scope=\"col\">Warehouse Code</th>\n" +
        "<th scope=\"col\"></th>\n";

    document.getElementById("body").innerHTML = data.map(function (val) {
        return "" +
            "<tr> " +
            "<th scope=\"row\">" + val.code + "</th>" +
            "<td>" + ((val.description===null) ? "":val.description) + "</td>" +
            "<td>" + "<input className=\"form-control\" id=\"orderQuantity\" type=\"number\" step=\"0.001\" min=\"0.000\">" + "</td>" +
            "<td>" + "<input className=\"form-control\" id=\"warehouseCode\" type=\"text\">" + "</td>" +
            "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: green; font-weight: bold' onclick='orderProduct(this)'>Order</i>" + "</td>" +
            "</tr>"
    }).join('') + "";
}
// ((val.description===null) ? "":val.description) + "</td>" +

function orderProduct(data) {
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onreadystatechange = function (xhr) {
        if (xhr.target.status === 200) {
            location.href='customerPanel.html';
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
    console.log(getCookie('access_token'));
    http_request.open('POST', url2, true);
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    var p = data.parentNode.parentNode;
    let productId = p.firstElementChild.textContent;
    let warehouseCode = p.children[3].firstElementChild.value;
    let orderQuantity = p.children[2].firstElementChild.value;
    let sendOrder = new NewOrder(warehouseCode, productId, orderQuantity);
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    http_request.send(JSON.stringify(sendOrder));
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

class NewOrder {
    basketItem = [];
    constructor(warehouseCode, productCode, quantity) {
        this.warehouseCode = warehouseCode;
        this.basketItem.push(new Item(productCode, quantity));
    }
}

class Item{
    constructor(productCode, quantity) {
       this.productCode = productCode;
       this.quantity = quantity;
    }
}

class Warehouse {
    constructor(code, description, address) {
        this.code = code;
        this.description = description;
        this.address = address;
    }
}


// function testowa() {
//     var adres = new Address("Kraków")
//     var magazyn1 = new Warehouse("01","cośtam",adres);
//     var magazyn2 = new Warehouse("02","cośtu",adres);
//     var lista = [magazyn1,magazyn2];
//     setTable(lista);
// }
