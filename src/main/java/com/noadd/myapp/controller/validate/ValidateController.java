package com.noadd.myapp.controller.validate;

import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.service.validate.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/validate")
@Transactional
public class ValidateController {

    @Autowired
    ValidateService validateService;

    @PostMapping("/getcode")
    public Map<String, Object> login(String sendType, String sendTo, String codeType) throws InterruptedException {
        validateService.createValidateCode(sendType, sendTo, codeType);
        return null;
    }
}
