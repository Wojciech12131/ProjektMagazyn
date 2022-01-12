package pl.edu.pk.auth.exception;

public class UserNotExist extends RuntimeException {
    public UserNotExist(String msg) {
        super("Użytkownik o podanym loginie " + msg + ", nie istnieje");
    }

}
