class Error {
    errorCode;
    errorMessage;
    status;
    errors;

    constructor(errorCode, errorMessage, status, errors) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
        this.errors = errors;
    }
}

function handleExceptions(response) {
    sessionStorage.setItem("error", response);
}

function showError() {
    let error = JSON.parse(sessionStorage.getItem("error"))
    if (error !== null) {
        setErrorAlert(error);
        sessionStorage.setItem("error", null);
    }
}


function setErrorAlert(error) {
    document.getElementById("errorMsg").innerHTML = "" +
        "<div class=\"alert alert-danger alert-dismissible fade show\" style=\"margin-top: 10px\" role=\"alert\">\n" +
        "     <strong>Error!</strong> " + error.errorMessage + "\n" +
        setValidationErrors(error) +
        "     <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "     <span aria-hidden=\"true\">&times;</span>\n" +
        "     </button>\n" +
        "</div>"
}

function clearError() {
    document.getElementById("errorMsg").innerHTML = "";
}

function setValidationErrors(error) {
    if (error.errors !== undefined) {
        return error.errors.map(function (arr) {
            return "- " + arr
        }).join("\n")
    } else
        return "";
}
