package com.sanshan.web.config.javaconfig.auxiliary;

import org.springframework.stereotype.Component;

/**
 *先暂时放在这里 这个是作为MQ的接受方使用的
 */
@Component
public class Receiver {


    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }

}
