package com.connectify.connectify.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Retrieve error details
        Object status = request.getAttribute("javax.servlet.error.status_code");
        String message = "An unexpected error occurred";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == 404) {
                message = "Page not found";
            } else if (statusCode == 500) {
                message = "Internal server error";
            }
        }

        model.addAttribute("message", message);
        return "error";
    }
}
