function Submit() {
    const url = "http://localhost:8000/mag/warehouse";
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


    let address = new Address(
        document.getElementById("emailWh").value,
        document.getElementById("mobileWh").value,
        document.getElementById("cityWh").value,
        document.getElementById("streetWh").value
    );
    let body = new CreateWarehouse(
        document.getElementById("codeWh").value,
        document.getElementById("descriptionWh").value,
        address,
    );
    http_request.open("POST", url, true);
    http_request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    if (getCookie('access_token') !== null && getCookie('access_token') !== "") {
        http_request.setRequestHeader('Authorization', 'Bearer ' + getCookie('access_token'));
    } else location.href = "index.html";
    http_request.send(JSON.stringify(body))

}

class CreateWarehouse {
    code;
    description;
    address;

    constructor(code, description, address) {
        this.code = code;
        this.description = description;
        this.address = address;
    }
}

class Address {
    email;
    mobile;
    city;
    street;

    constructor(email, mobile, city, street) {
        this.email = email;
        this.mobile = mobile;
        this.city = city;
        this.street = street;
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
