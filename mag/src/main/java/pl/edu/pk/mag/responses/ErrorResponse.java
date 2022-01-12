package pl.edu.pk.mag.responses;

import lombok.Data;
import pl.edu.pk.mag.exceptions.ApplicationException;

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
