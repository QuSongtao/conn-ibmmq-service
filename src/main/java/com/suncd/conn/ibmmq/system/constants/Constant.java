/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.system.constants;

public class Constant {

    private Constant(){

    }

    // ------------------------------------ MQ CONSTANTS ------------------------------------
    // 队列管理器状态 0-停止 1-运行
    public static int QMGR_STATUS = 0;

    // 通信方式
    public static String CONN_MQ = "MQ";    // MQ通信
    public static String CONN_SK = "SK";    // Socket通信

    // MQ通信协议
    public static String MQ_JMS = "JMS";    // JMS协议
    public static String MQ_PTP = "PTP";    // PTP协议

    // 各通信系统编码
    public static String ERP_CODE = "L4";    // 产销ERP
    public static String MES_CR = "CR";      // 冷轧1#MES
    public static String MES_2130 = "2130";  // 2130
    public static String MES_2150 = "2150";  // 2150
    public static String MES_1700 = "1700";  // 1700
    public static String MES_1780 = "1780";  // 1780
    public static String MES_1450 = "1450";  // 1450
    public static String MES_TEST = "TEST";  // 检化验系统
    public static String MES_ASL = "ASL";    // 鞍神高强线
    public static String L2_GAL1 = "GAL1";   // 1#镀锌
    public static String L2_GAL3 = "GAL3";   // 3#镀锌
    public static String L2_CF = "CF";       // 清洗
    public static String L2_PCSAW = "PCSAW"; // 酸洗
    public static String L2_CC = "CC";       // 彩涂

    /**
     *       # 本地监听队列列表
     *       2130.CR.M.1       # 冷轧2130线----------MES
     *       2150.CR.M.1       # 热轧2150线
     *       ASP_1700.CR.M.1   # 热轧1700线
     *       HR_1780.CR.M.1    # 热轧1780线
     *       LZ4M1.CR.M.1      # 冷轧1450线
     *       TEST.CR           # 检化验系统
     *       ASL.CR.Q          # 冷轧鞍神高强线
     *       ERP.CR.O.OK28     # 产销ERP系统 --------ERP
     *       ERP.CR.O.OL28     # 产销ERP系统
     *       ERP.CR.O.OO28     # 产销ERP系统
     *       ERP.CR.Q.1        # 产销ERP系统
     *       ERP.CR.Q.2        # 产销ERP系统
     *       ERP.CR.W.BUL      # 产销ERP系统
     *       ERP.CR.W.OTHER    # 产销ERP系统
     *       GAL1.CR.Q         # 冷轧1#镀锌线---------L2
     *       GAL3.CR.Q         # 冷轧3#镀锌线
     *       CF.CR.Q           # 冷轧1#线清洗机组
     *       PCSAW.CR.Q        # 冷轧1#线酸洗机组
     *       CC.CR.M.1         # 冷轧彩涂线
     */

}
