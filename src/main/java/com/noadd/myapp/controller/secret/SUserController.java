package com.noadd.myapp.controller.secret;

import com.noadd.myapp.util.baseUtil.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/japi/secret")
public class SUserController {
//    @Autowired
//    private SUerService sUerService;

    @GetMapping("/login")
    public Map<String, String> login(String userName, String userPass) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName, userPass)) {
            code = "0001";
        } else {

        }
        out.put("code", code);
        return out;
    }
}
