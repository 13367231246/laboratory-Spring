package com.hss.controller;

import com.hss.pojo.Result;
import com.hss.pojo.contactUs;
import com.hss.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;
    //添加信息
    @PostMapping("/add")
    public Result add(@RequestBody contactUs contact) {
        contactService.add(contact);
        return Result.success();
    }
    //查询信息
    @GetMapping("/query")
    public Result query() {
        contactUs contact = contactService.query();
        return Result.success(contact);
    }

}
