package com.noadd.myapp.controller.validate;

import com.noadd.myapp.mailservice.MyMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/validate")
public class ValidateController {

    @PostMapping("/getcode")
    public Map<String, Object> login(String sendType, String sendTo, String codeType) throws InterruptedException {
        return null;
    }
}
