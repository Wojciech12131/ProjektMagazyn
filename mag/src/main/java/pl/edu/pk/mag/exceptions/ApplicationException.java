package pl.edu.pk.mag.exceptions;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final int status;

    public ApplicationException(String errorCode, int status, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.status = status;
    }

}
