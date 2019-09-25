
package com.suncd.conn.ibmmq.system.configuration;


import com.ibm.mq.jms.MQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

/**
 * (废弃)
 */
@Deprecated
//@Configuration
public class IbmMqJmsConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(IbmMqJmsConfiguration.class);

    /*@Value("${ibm.mq.queue-manager}")
    private String qmName;
    @Value("${ibm.mq.hostDef}")
    private String host;
    @Value("${ibm.mq.portDef}")
    private int port;
    @Value("${ibm.mq.channel}")
    private String channel;
    @Value("${ibm.mq.user}")
    private String user;
    @Value("${ibm.mq.password}")
    private String password;
    @Value("${ibm.mq.ccsidDef}")
    private int ccsid;*/

    /*
    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();

        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            mqQueueConnectionFactory.setCCSID(1208);
            mqQueueConnectionFactory.setChannel(channel);
            mqQueueConnectionFactory.setPort(port);
            mqQueueConnectionFactory.setQueueManager(queueManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }

    @Bean(name = "UserCredentialsConnectionFactoryAdapter1")
    public UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter() {
        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        userCredentialsConnectionFactoryAdapter.setUsername("MUSR_MQADMIN");
        userCredentialsConnectionFactoryAdapter.setPassword("123456");
        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqConnectionFactory);
        return new UserCredentialsConnectionFactoryAdapter();
    }

    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(MQConnectionFactory mqConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(mqConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(500);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(CachingConnectionFactory cachingConnectionFactory){
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(cachingConnectionFactory);
        return jmsTransactionManager;
    }*/

    @Bean
    public JmsOperations jmsOperations(MQConnectionFactory mqConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(mqConnectionFactory);
        jmsTemplate.setReceiveTimeout(20000);
        LOGGER.info("初始化JMS模板完成!");
        return jmsTemplate;
    }

//    @Bean
//    public MQQueueManager getMQQueueManager() {
//        MQEnvironment.hostname = host;
//        MQEnvironment.port = port;
//        MQEnvironment.userID = user;
//        MQEnvironment.password = password;
//        MQEnvironment.channel = channel;
//        MQEnvironment.CCSID = ccsid;
//        MQQueueManager mqQueueManager = null;
//        try {
//            mqQueueManager = new MQQueueManager(qmName);
//        } catch (MQException e) {
//            LOGGER.error("初始化队列管理器出现异常",e);
//        }
//        LOGGER.info("初始化队列管理器完成!");
//        return mqQueueManager;
//    }

}
