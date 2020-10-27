package org.example.other;

import java.util.concurrent.CountDownLatch;

public class NongWanJia {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i ++) {

        }
    }
}

class Client implements Runnable {

    @Override
    public void run() {
//        curl 'http://b2b.api.nongwanjia.net/h5/login/send_register_sms' \
//        -H 'Connection: keep-alive' \
//        -H 'User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1' \
//        -H 'x-token: undefined' \
//        -H 'Content-Type: application/x-www-form-urlencoded' \
//        -H 'Accept: */*' \
//        -H 'Origin: http://h5.nongwanjia.net' \
//        -H 'Referer: http://h5.nongwanjia.net/register' \
//        -H 'Accept-Language: zh-CN,zh;q=0.9,en;q=0.8' \
//        --data-raw 'phone=13333333333' \
//        --compressed \
//        --insecure
        String url = "http://b2b.api.nongwanjia.net/h5/login/send_register_sms";

    }
}
