package com.sso.shiroweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile")
public class MobileController {

    @RequestMapping("/query")
    public String query(){
        return "mobile";
    }
}
