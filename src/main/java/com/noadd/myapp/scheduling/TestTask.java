package com.noadd.myapp.scheduling;

import com.noadd.myapp.util.baseUtil.HttpUtil;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private void process() throws Exception {
        System.out.println("现在时间：" + TimeUtil.getCurrentDate(null));
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/815566704",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/961064193",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/291244026",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/2048787831",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/910615337",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/1987047448",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/747837043",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/3539904402",null,null,null);
        HttpUtil.doGet("http://www.apesing.com/japi/mpz/add/951423080",null,null,null);
    }
}
