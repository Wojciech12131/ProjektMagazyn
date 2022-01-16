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
    NOT_FOUND_PERMISSION("NOT_FOUND_PERMISSION", 404, "Podano niepoprawne uprawnienie"),
    REMOVE_AND_MOVE_NOT_POSSIBLE("REMOVE_AND_MOVE_NOT_POSSIBLE", 400, "Nie można jednocześnie usunąć i przenieść gdzieś indziej produktu."),
    NOT_FOUND_SHELF("NOT_FOUND_SHELF", 404, "Nie znaleziono wskazanej półki w danym magazynie"),
    NOT_FOUND_DESTINATION_SHELF("NOT_FOUND_DESTINATION_SHELF", 404, "Nie znaleziono półki docelowej"),
    DESTINATION_SHELF_IS_NOT_EMPTY("DESTINATION_SHELF_IS_NOT_EMPTY", 400, "Na docelowej półce znajduję się już produkt."),
    DESTINATION_SHELF_IS_EMPTY("DESTINATION_SHELF_IS__EMPTY", 400, "Na docelowej półce nie znajduję się produkt, nie można dodać do niego ilości."),
    NOT_FOUND_PRODUCT("NOT_FOUND_PRODUCT", 404, "Nie znaleziono prodkuktu o podanym kodzie."),
    SHELF_CODE_ID_EMPTY_OR_NULL("SHELF_CODE_ID_EMPTY_OR_NULL", 400, "Kod półki nie może być pusty."),
    SHELF_ALREADY_EXISTED("SHELF_ALREADY_EXISTED", 400, "Istnieje już półka o danym kodzie."),
    ADD_QUANTITY_IS_NOT_POSSIBLE("ADD_QUANTITY_IS_NOT_POSSIBLE", 400, "Nie można dodać ilości do edytowanego produku."),
    SHELF_IS_NOT_EMPTY("SHELF_IS_NOT_EMPTY", 400, "Półka zawiera produkt, proszę się go pozbyć przed daną akcją."),
    NOT_FOUND_ORDER("NOT_FOUND_ORDER", 404, "Nie znaleziono podanego zamówienia."),
    INVALID_ORDER_STATUS("INVALID_ORDER_STATUS", 400, "Podane zamównie ma nieprawidłowy status."),
    NOT_FOUND_MEMBER("NOT_FOUND_MEMBER", 404, "Nie znaleziono użytkownika w podanej grupie.");


    final ApplicationException applicationException;

    AppException(String errorCode, int status, String msg) {
        this.applicationException = new ApplicationException(errorCode, status, msg);

    }

    public ApplicationException getError() {
        return applicationException;
    }
}
