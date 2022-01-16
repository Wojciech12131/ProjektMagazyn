var url = "http://localhost:8000/mag/warehouse/code/";

function loadProductList() {
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
    if (sessionStorage.getItem('code') !== null && sessionStorage.getItem('code') !== "") {
        http_request.open('GET', url + sessionStorage.getItem('code'), true);
    }
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    http_request.send(null);
}

function setTable(data) {
    document.getElementById("column").innerHTML = "" +
        "<th scope=\"col\">Shelf</th>\n" +
        "<th scope=\"col\">Product</th>\n" +
        "<th scope=\"col\">Quantity</th>\n" +
        "<th scope=\"col\">Product Dectription</th>" +
        "<th scope=\"col\">Edit shelf</th>" +
        "<th scope=\"col\">Remove shelf</th>";

    document.getElementById("body").innerHTML = data.map(function (val) {
        return "" +
            "<tr> " +
            "<th scope=\"row\">" + val.code + "</th>" +
            "<td>" + ((val.product===null) ? "": val.product.code) + "</td>" +
            "<td>" + val.quantity + "</td>" +
            "<td>" + ((val.product===null || val.product.description===null) ? "": val.product.description) + "</td>" +
            "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: orange' onclick= 'editShelf(this)'>edit</i>" + "</td>" +
            "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: red' onclick='removeElement(this)'>remove</i>" + "</td>" +
            "</tr>"
    }).join('');
}
function editShelf(data) {
    var p = data.parentNode.parentNode;
    sessionStorage.setItem("shelfCode", p.children[0].textContent);
    sessionStorage.setItem("productCode", p.children[1].textContent);
    sessionStorage.setItem("productQuantity", p.children[2].textContent);
    sessionStorage.setItem("productDescription", p.children[3].textContent);
    location.href = "editShelf.html";
}
function addShelf(){
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;

    http_request.onreadystatechange = function (xhr) {
        if(http_request.readyState === 4) {
            if (xhr.target.status === 204) {
                addingSection(document.getElementById("input").value);
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
        }
    };
    if (sessionStorage.getItem('code') !== null && sessionStorage.getItem('code') !== "") {
        http_request.open('GET', url + sessionStorage.getItem('code') +"/addShelf?shelfCode="+ document.getElementById("input").value, true);
    }
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    http_request.send(null);
}

// function addAdminColumn() {
//     if (isAdminUser())
//         return "\n<th scope=\"col\">Usuń</th>\n"
//     return ""
// }

// function addRemoveButton() {
//     if (isAdminUser())
//         return "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: red' onclick='removeElement(this)'>delete</i>" + "</td>"
//     else
//         return ""
// }

function addingSection(code) {
    document.getElementById("body").innerHTML =  document.getElementById("body").innerHTML +
        "<tr> " +
        "<th scope=\"row\">" + code + "</th>" +
        "<td>" + "" + "</td>" +
        "<td>" + 0.0 + "</td>" +
        "<td>" + "" + "</td>" +
        "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: orange' onclick= location.href='editShelf.html'>edit</i>" + "</td>" +
        "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: red' onclick='removeElement(this)'>remove</i>" + "</td>" +
        "</tr>"
}


function logout() {
    delete_cookie("access_token");
    location.href = 'index.html';
}

function removeElement(element) {
    var p = element.parentNode.parentNode
    let id = p.firstElementChild.textContent
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true
    http_request.onload = function (xhr) {
        if (xhr.target.status === 204) {
            clearError();
            p.parentNode.removeChild(p);
        } else if (xhr.target.status === 404) {
            p.parentNode.removeChild(p);
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    };
    http_request.open('DELETE', url + sessionStorage.getItem('code') +"/removeShelf?shelfCode="+ id, true);
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    http_request.send(null);
}


function addElement() {
    let title = document.getElementById("bookTitle").value;
    let author = document.getElementById("bookAuthor").value;
    let year = document.getElementById("bookYear").value;
    let newBook = new Book(title, author, year);
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true

    http_request.onload = function (xhr) {
        if (xhr.target.status === 200) {
            clearError();
            var data = JSON.parse(xhr.target.response);
            setTable(data);
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    };
    http_request.open('POST', url, true);
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    http_request.send(JSON.stringify(newBook));
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function delete_cookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function clearSessionStorage() {
    sessionStorage.setItem('shelfCode', "");
    sessionStorage.setItem('productCode', "");
    sessionStorage.setItem('productQuantity', 0.0);
    sessionStorage.setItem('productDescription', "");
}


