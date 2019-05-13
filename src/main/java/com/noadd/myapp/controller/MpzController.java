package com.noadd.myapp.controller;

import com.noadd.myapp.Thread.MyQueen;
import com.noadd.myapp.Thread.ThreadTemp;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/japi/mpz")
public class MpzController {
    public static MyQueen mpzQueen = new MyQueen();
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @RequestMapping("/{qq}")
    public Map<String, String> mpz(@PathVariable String qq) {
        Map<String, String> out = new HashMap<>();
        mpzQueen.put(qq);
        out.put("code", "0000");
        return out;
    }

    public void test() {
        executorService.submit(new ThreadTemp((String) mpzQueen.get()));
    }

}
