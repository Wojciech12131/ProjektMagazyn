package pl.edu.pk.mag.exceptions;

public enum AppException {
    NOT_FOUND_USER("NOT_FOUND", 404, "Nie znaleziono użytkownika o podanym loginie."),
    AUTH_ERROR("ACCESS_DENIED", 403, "Brak dostępu."),
    USERNAME_NOT_UNIQUE("USERNAME_NOT_UNIQUE", 400, "Nie unikatowa nazwa użytkownika."),
    LOGIN_ERROR("LOGIN_ERROR", 403, "Podano nieporawny login lub hasło."),
    NOT_FOUND_BOOK("NOT_FOUND", 404, "Nie znaleziono książki o podanym id."),
    INVALID_PATH_VARIABLE("INVALID_PATH_VARIABLE", 400, "Podano niepoprawną wartość zmiennej w adresie.");


    final ApplicationException applicationException;

    AppException(String errorCode, int status, String msg) {
        this.applicationException = new ApplicationException(errorCode, status, msg);

    }

    public ApplicationException getError() {
        return applicationException;
    }
}
