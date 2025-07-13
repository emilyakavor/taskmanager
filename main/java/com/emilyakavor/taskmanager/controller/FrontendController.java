package com.emilyakavor.taskmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class FrontendController {
    @GetMapping(value = "/{path:^(?!api|assets|static|index\\.html).*$}")
    public String forward(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("Forwarding to frontend");
        return "forward:/";
    }
}

