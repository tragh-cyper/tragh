package com.sso.shiroweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @RequestMapping("/query")
    public String query(){
        return "salary";
    }


}
