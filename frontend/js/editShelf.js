var url = "http://localhost:8000/mag/warehouse/code/";


    // if (sessionStorage.getItem('code') !== null && sessionStorage.getItem('code') !== "") {
    //     http_request.open('POST', url + sessionStorage.getItem('code') + "/modifyShelf", true);
    // }

function changeQuantity(){
    let http_request = new XMLHttpRequest();
    http_request.withCredentials = true;
    http_request.onreadystatechange = function (xhr) {
        if(http_request.readyState === 4) {
            if (xhr.target.status === 204) {
                location.href='storageProductList.html'
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
        http_request.open('POST', url + sessionStorage.getItem('code') + "/modifyShelf", true);
    }
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    }
    else location.href = "index.html";
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    let sendQauntity = new ModifyShelf();
    sendQauntity.code = sessionStorage.getItem('shelfCode');
    sendQauntity.addQuantity = new AddQuantity(document.getElementById('changeQuantity').value);
    http_request.send(JSON.stringify(sendQauntity));
}



function logout() {
    delete_cookie("access_token");
    location.href = 'index.html';
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

class ModifyShelf {
    code;
    moveProduct;
    addProduct;
    addQuantity;
    removeProduct;
}
class AddQuantity {
    constructor(quantity) {
        this.quantity=quantity;
    }
}

class MoveProduct {
    constructor(code) {
        this.destinationShelfCode=code;
    }
}

class AddProduct {
    code;
    dest;
    quantity;

    set code(value) {
        this.code = value;
    }

    set dest(value) {
        this.dest = value;
    }

    set quantity(value) {
        this.quantity = value;
    }
}
