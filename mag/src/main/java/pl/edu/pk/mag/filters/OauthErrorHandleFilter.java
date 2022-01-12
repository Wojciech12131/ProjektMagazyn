package pl.edu.pk.mag.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.responses.ErrorResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class OauthErrorHandleFilter implements Filter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (ApplicationException applicationException) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(applicationException.getStatus());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            objectMapper.writeValue(httpServletResponse.getWriter(), new ErrorResponse(applicationException));

        }
    }
}
