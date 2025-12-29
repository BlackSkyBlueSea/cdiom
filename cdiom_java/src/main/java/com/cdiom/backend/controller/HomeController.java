package com.cdiom.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 首页控制器
 *
 * @author cdiom
 */
@Controller
public class HomeController {

    @Value("${server.port:8080}")
    private String serverPort;

    private String apiBaseUrl = "/api/v1";

    /**
     * 首页
     */
    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("projectName", "CDIOM医药管理系统");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("serverPort", serverPort);
        model.addAttribute("apiBaseUrl", apiBaseUrl);
        model.addAttribute("status", "运行正常");
        model.addAttribute("serverTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return "index";
    }
}

