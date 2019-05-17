package com.noadd.myapp.scheduling;

import com.noadd.myapp.util.baseUtil.FileUtil;
import com.noadd.myapp.util.baseUtil.HttpUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class TestTask {

    //    @Scheduled(fixedRate = 60000)
    /*
    @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行；
    @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行；
    @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次。
    */
    public void reportCurrentTime() {
        System.out.println("现在时间：" + TimeUtil.getCurrentDate(null));
    }

    @Scheduled(cron = "0 0 0/1 1/1 * ? ")
    private void process() {
        System.out.println("现在时间：" + TimeUtil.getCurrentDate(null));
//        String filePath = Objects.requireNonNull(TestTask.class.getClassLoader().getResource("mpzTaskList.txt")).getPath();
        String filePath = "/opt/mpzTaskList.txt";
        List<String> qqList = FileUtil.readFileByLine(filePath);
        qqList.forEach(qq -> {
            if (!StringUtil.isEmpty(qq))
                try {
                    HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/" + qq, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }
}
