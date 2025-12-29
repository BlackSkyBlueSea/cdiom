package com.cdiom.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 首页控制器
 * 用于展示项目运行成功信息
 *
 * @author cdiom
 */
@Controller
public class IndexController {

    /**
     * 首页 - 展示项目运行成功信息
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("projectName", "CDIOM医药管理系统");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("status", "运行正常");
        model.addAttribute("serverTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        model.addAttribute("apiBaseUrl", "/api/v1");
        model.addAttribute("serverPort", 8080);
        return "index";
    }
}

