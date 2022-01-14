package pl.edu.pk.mag.exceptions;

public enum AppException {
    NOT_FOUND_USER("NOT_FOUND", 404, "Nie znaleziono użytkownika o podanym loginie."),
    NOT_FOUND_WAREHOUSE("NOT_FOUND_WAREHOUSE", 404, "Nie znaleziono magazynu o podanym kodzie."),
    AUTH_ERROR("ACCESS_DENIED", 403, "Brak dostępu."),
    USERNAME_NOT_UNIQUE("USERNAME_NOT_UNIQUE", 400, "Nie unikatowa nazwa użytkownika."),
    WAREHOUSE_CODE_NOT_UNIQUE("WAREHOUSE_CODE_NOT_UNIQUE", 400, "Nie unikatowa nazwa magazynu."),
    LOGIN_ERROR("LOGIN_ERROR", 403, "Podano nieporawny login lub hasło."),
    NOT_FOUND_BOOK("NOT_FOUND", 404, "Nie znaleziono książki o podanym id."),
    INVALID_PATH_VARIABLE("INVALID_PATH_VARIABLE", 400, "Podano niepoprawną wartość zmiennej w adresie."),
    NOT_FOUND_PERMISSION("NOT_FOUND_PERMISSION", 404, "Podano niepoprawne uprawnienie");


    final ApplicationException applicationException;

    AppException(String errorCode, int status, String msg) {
        this.applicationException = new ApplicationException(errorCode, status, msg);

    }

    public ApplicationException getError() {
        return applicationException;
    }
}
