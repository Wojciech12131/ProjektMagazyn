package pl.edu.pk.mag.exceptions.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.responses.ErrorResponse;
import pl.edu.pk.mag.responses.ValidationError;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleError(Exception e, WebRequest request) {
        if (e instanceof ApplicationException appError) {
            return new ResponseEntity<>(new ErrorResponse(appError), HttpStatus.valueOf(appError.getStatus()));
        } else
            return new ResponseEntity<>(new ErrorResponse(e), HttpStatus.valueOf(500));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, ValidationError.ValidationErrorBuilder.fromBindingErrors(ex.getBindingResult()), headers, status, request);
    }
}
