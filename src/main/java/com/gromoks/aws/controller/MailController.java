package com.gromoks.aws.controller;

import com.gromoks.aws.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mails")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public ResponseEntity send() {
        mailService.send();
        return ResponseEntity.noContent().build();
    }

}
