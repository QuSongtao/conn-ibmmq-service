/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.service.messageservice.listener;

import com.ibm.jms.JMSBytesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * 消息侦听
 * 可以根据业务量大小扩展消息侦听器
 *
 * @author qust
 * @version 1.0 20180927
 */
@Component
public class ReceiveMessage extends MessageListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveMessage.class);
    private static final Logger WARN_LOGGER = LoggerFactory.getLogger("warnAndErrorLogger");

    @Override
    @JmsListeners(value = {
            @JmsListener(destination = "${ibm.recv.queue.q1}")//, // xxx队列
            //@JmsListener(destination = "Q2")                    // xxx队列
    })
    public void onMessage(Message message) {
        // 1.监听并读取消息
        String recvStrMsg = "";
        if (message instanceof JMSBytesMessage) {
            WARN_LOGGER.info("字节类型的消息");
            JMSBytesMessage bm = (JMSBytesMessage) message;
            byte[] bys;
            try {
                bys = new byte[(int) bm.getBodyLength()];
                bm.readBytes(bys);
                recvStrMsg = new String(bys);
            } catch (Exception e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        } else {
            WARN_LOGGER.info("文本类型的消息");
            TextMessage bm = (TextMessage) message;
            try {
                recvStrMsg = bm.getText();
            } catch (JMSException e) {
                WARN_LOGGER.error(e.getMessage(), e);
            }
        }

        // 2.记录消息日志
        LOGGER.info("收到消息:{}", recvStrMsg);
    }

    @Override
    protected void handleListenerException(Throwable e) {
        WARN_LOGGER.error(e.getMessage(), e);
    }
}
