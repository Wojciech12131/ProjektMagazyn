package pl.edu.pk.mag.exceptions.handlers;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pl.edu.pk.mag.exceptions.ApplicationException;

import java.io.IOException;

public class ResponseErrorHandlerImpl implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getRawStatusCode() >= 400;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getRawStatusCode() == 404) {
            throw new ApplicationException("AUTH_EXCEPTION", 500, "Problem z dostępem do serwisu autoryzacji. Proszę spróbować później.");
        } else if (response.getRawStatusCode() == 400) {
            throw new ApplicationException("INVALID_TOKEN", 400, "Podano niepoprawny lub wygasły token, proszę zalogować się ponownie.");
        } else
            throw new ApplicationException("OTHER_ERROR", 500, "Niespodziewany błąd serwisu autoryzacji, prosimy spróbować później.");
    }
}
