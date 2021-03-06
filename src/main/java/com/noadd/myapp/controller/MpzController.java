package com.noadd.myapp.controller;

import com.noadd.myapp.Thread.MyQueen;
import com.noadd.myapp.Thread.ThreadTemp;
import com.noadd.myapp.util.baseUtil.FileUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/japi/mpz")
public class MpzController {
    private static MyQueen mpzQueen = new MyQueen(0, 99);
    private static ExecutorService fixedExecutorService;
    private static ExecutorService singleExecutorService;
    private static boolean state = false;

    @RequestMapping("/add/{qq}")
    public Map<String, String> mpz(@PathVariable String qq) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (mpzQueen.put(qq) != 1) {
            code = "8000";
        }
        out.put("code", code);
        return out;
    }

    @RequestMapping("/task/{qq}/{psw}")
    public Map<String, String> mpz(@PathVariable String qq, @PathVariable String psw) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if ("815566704".equals(psw)) {
//            String filePath = Objects.requireNonNull(FileUtil.class.getClassLoader().getResource("mpzTaskList.txt")).getPath();
            String filePath = "/opt/mpzTaskList.txt";
            FileUtil.insert(filePath, "\r\n" + qq);
        } else {
            code = "8999";
        }
        out.put("code", code);
        return out;
    }

    @RequestMapping("/start/{psw}")
    public Map<String, String> start(@PathVariable String psw) {
        Map<String, String> out = new HashMap<>();
        String code = "8999";
        if ("815566704".equals(psw) && !state) {
            fixedExecutorService = Executors.newFixedThreadPool(10);
            singleExecutorService = Executors.newSingleThreadExecutor();
            singleExecutorService.execute(() -> {
                for (int i = 0; i < 10; i++) {
                    fixedExecutorService.submit(new ThreadTemp((String) mpzQueen.get()));
                }
                fixedExecutorService.shutdown();
                while (true) {
                    if (fixedExecutorService.isTerminated()) {
                        fixedExecutorService = Executors.newFixedThreadPool(10);
                        for (int i = 0; i < 10; i++) {
                            fixedExecutorService.submit(new ThreadTemp((String) mpzQueen.get()));
                        }
                        fixedExecutorService.shutdown();
                    }
                }

            });
            code = "0000";
            state = true;
        }
        out.put("code", code);
        return out;
    }

    @RequestMapping("/close/{psw}")
    public Map<String, String> close(@PathVariable String psw) {
        Map<String, String> out = new HashMap<>();
        String code = "8999";
        if ("815566704".equals(psw) && state) {
            singleExecutorService.shutdownNow();
            fixedExecutorService.shutdownNow();
            code = "0000";
            state = false;
        }
        out.put("code", code);
        return out;
    }

    @RequestMapping("/get")
    public Map<String, String> getAll() {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        out.put("data", mpzQueen.getAllStr());
        out.put("code", code);
        return out;
    }


}
