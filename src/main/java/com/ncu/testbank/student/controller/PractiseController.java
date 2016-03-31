package com.ncu.testbank.student.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;

@Api(value = "practise-api", description = "练习模块", position = 4)
@RestController
@RequestMapping("/student")
public class PractiseController {

}
