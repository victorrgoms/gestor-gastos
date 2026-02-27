package org.victor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    // Endpoint so pro robo do cron-job bater e receber um 200 OK
    @GetMapping("/")
    public String ping() {
        return "API rodando!";
    }
}