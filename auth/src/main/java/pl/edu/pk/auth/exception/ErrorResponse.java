package pl.edu.pk.auth.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
    private int status;

    public ErrorResponse(ApplicationException applicationException) {
        this.errorCode = applicationException.getErrorCode();
        this.errorMessage = applicationException.getLocalizedMessage();
        this.status = applicationException.getStatus();
    }

    public ErrorResponse(Exception e) {
        this.errorCode = "OTHER_ERROR";
        this.errorMessage = e.getLocalizedMessage();
        this.status = 500;
    }
}

