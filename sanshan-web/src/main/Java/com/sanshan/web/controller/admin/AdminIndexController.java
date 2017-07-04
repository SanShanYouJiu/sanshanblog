package com.sanshan.web.controller.admin;

import com.sanshan.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/index")
@PreAuthorize("hasRole('USER')")
public class AdminIndexController {

    @Autowired
    private BlogService blogService;



}
