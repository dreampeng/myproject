package com.noadd.myapp.scheduling;

import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class TestTask {

    @Scheduled(fixedRate = 6000)
    /*
    @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行；
    @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行；
    @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次。
    */
    public void reportCurrentTime() {
        System.out.println("现在时间：" + TimeUtil.getCurrentDate(null));
    }

    private int count = 0;

    @Scheduled(cron = "*/6 * * * * ?")
    private void process() {
        System.out.println("this is scheduler task runing  " + (count++));
    }
}
