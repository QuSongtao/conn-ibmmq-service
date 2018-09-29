/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.controller;

import com.suncd.conn.ibmmq.service.statusservice.QmgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qmgr")
public class QmgrController {

    @Autowired
    private QmgrService qmgrService;

    @RequestMapping(value = "/channel/status",method = RequestMethod.GET)
    public String getChlStatus(String channelName){
        int code = qmgrService.getChannelStatus(channelName);
        return "通道状态:" + code;
    }

    @RequestMapping(value = "/queue/depth",method = RequestMethod.GET)
    public String getQueueStatus(String qName){
        int code = qmgrService.getLocalQueueDepth(qName);
        return "队列深度:" + code;
    }
}
