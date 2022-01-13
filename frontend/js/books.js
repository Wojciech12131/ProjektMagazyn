var url = "http://localhost:8080/dashboard";
var logoutUrl = "http://localhost:8080/logout"

function loadBookList() {
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
                data.errorMessage = "Brak sesji, proszę zalogować się ponownie"
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
        "<th scope=\"col\">id</th>\n" +
        "<th scope=\"col\">Tytuł</th>\n" +
        "<th scope=\"col\">Autor</th>\n" +
        "<th scope=\"col\">Rok wydania</th>" + addAdminColumn();

    document.getElementById("body").innerHTML = data.map(function (val) {
        return "" +
            "<tr> " +
            "<th scope=\"row\">" + val.id + "</th>" +
            "<td>" + val.title + "</td>" +
            "<td>" + val.author + "</td>" +
            "<td>" + val.year + "</td>" + addRemoveButton() +
            "</tr>"
    }).join('') + addingSection();
}

function addAdminColumn() {
    if (isAdminUser())
        return "\n<th scope=\"col\">Usuń</th>\n"
    return ""
}

function addRemoveButton() {
    if (isAdminUser())
        return "<td>" + "<i class=\"material-icons\" style='cursor: pointer;color: red' onclick='removeElement(this)'>delete</i>" + "</td>"
    else
        return ""
}

function addingSection() {
    if (isAdminUser()) {
        return "<tr>\n" +
            "       <th scope=\"row\">#</th>\n" +
            "       <td><input type=\"text\" class=\"form-control\" id=\"bookTitle\"  placeholder=\"Tytuł książki\"></td>\n" +
            "       <td><input type=\"text\" class=\"form-control\" id=\"bookAuthor\"  placeholder=\"Autor książki\"></td></td>\n" +
            "       <td><input type=\"number\" class=\"form-control\" id=\"bookYear\"  placeholder=\"Rok wydania\"></td></td></td>\n" +
            "       <td><i class=\"material-icons\" style='cursor: pointer;color: #1eff20'  onclick='addElement()'>done</i></td>\n" +
            "  </tr>"
    } else
        return ""
}

function logout() {
    delete_cookie("ROLE");
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onload = function (xhr) {
        if (xhr.target.status === 204) {
            clearError();
            console.log("Poprawnie wylogowano")
            location.href = "index.html"
        } else if (xhr.target.status === 403) {
            if (data.errorCode === "ACCESS_DENIED") {
                data.errorMessage = "Brak sesji, proszę zalogować się ponownie"
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

function removeElement(element) {
    var p = element.parentNode.parentNode
    let id = p.firstElementChild.textContent
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true

    http_request.onload = function (xhr) {
        if (xhr.target.status === 200) {
            clearError();
            p.parentNode.removeChild(p);
        } else if (xhr.target.status === 404) {
            p.parentNode.removeChild(p);
        } else {
            handleExceptions(xhr.target.response);
            showError();
        }
    };
    http_request.open('DELETE', url + "/" + id, true);
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

function isAdminUser() {
    return getCookie("ROLE") === "ROLE_ADMIN";
}

class Book {
    id;

    constructor(title, author, year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }
}
