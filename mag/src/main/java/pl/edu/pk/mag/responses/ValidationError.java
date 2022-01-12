package pl.edu.pk.mag.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private final String errorCode = "VALIDATION_ERROR";

    private final String errorMessage = "Wykryto błędy walidacji.";

    private final Integer status = 400;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

    public void addValidationError(String error) {
        errors.add(error);
    }

    public static class ValidationErrorBuilder {

        public static ValidationError fromBindingErrors(Errors errors) {
            ValidationError error = new ValidationError();
            for (ObjectError objectError : errors.getAllErrors()) {
                error.addValidationError((" " + objectError.getDefaultMessage()));
            }
            return error;
        }
    }
}