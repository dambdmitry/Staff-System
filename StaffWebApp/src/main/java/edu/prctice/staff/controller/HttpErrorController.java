package edu.prctice.staff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class HttpErrorController implements ErrorController {

    private final MessageSource messageSource;

    @Autowired
    public HttpErrorController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping("/error")
    public String handleError(Locale locale, Model model, HttpServletRequest request, Exception ex) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {

            int statusCode = Integer.valueOf(status.toString());

            // 403
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "HttpErrors/noAccess";
            }

            // 404
            else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "HttpErrors/notFound";
            }

            // 405
            else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                return "HttpErrors/noAccess";
            }

            // 500
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "HttpErrors/serverError";
            }

        }

        return "HttpErrors/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
