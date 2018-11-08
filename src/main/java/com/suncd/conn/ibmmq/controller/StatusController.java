package com.suncd.conn.ibmmq.controller;

import com.suncd.conn.ibmmq.service.statusservice.ConnObjectService;
import com.suncd.conn.ibmmq.service.statusservice.QmgrService;
import com.suncd.conn.ibmmq.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mq/status")
public class StatusController {

    @Autowired
    private QmgrService qmgrService;
    @Autowired
    private ConnObjectService connObjectService;

    @RequestMapping(value = "/channel", method = RequestMethod.GET)
    public Response getChlStatus(int pageIndex, int pageSize) {
        return connObjectService.getObjByType("CHANNEL", null, pageIndex, pageSize);

    }

    /**
     * 队列只取本地队列
     */
    @RequestMapping(value = "/queue", method = RequestMethod.GET)
    public Response getQueueStatus(int pageIndex, int pageSize) {
        return connObjectService.getObjByType("QUEUE", "R", pageIndex, pageSize);
    }

    @RequestMapping(value = "/qmgr", method = RequestMethod.GET)
    public Map getQmgrStatus() {
        return qmgrService.getQmgrStatus();
    }
}
