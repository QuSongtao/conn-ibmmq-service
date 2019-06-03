package com.suncd.conn.ibmmq.controller;

import com.suncd.conn.ibmmq.service.statusservice.ConnObjectService;
import com.suncd.conn.ibmmq.service.statusservice.QmgrService;
import com.suncd.conn.ibmmq.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Response getChlStatus(String transferType, int pageIndex, int pageSize) {
        return connObjectService.getObjByType("CHANNEL", transferType, pageIndex, pageSize);
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

    @RequestMapping(value = "/ctl", method = RequestMethod.GET)
    public Response ctlChl(String chlName, int handleCode) {
        int compCode = qmgrService.controlChannel(chlName, handleCode);
        if (0 == compCode){
            return new Response<>().success("操作成功");
        }else{
            return new Response<>().failure("操作失败,请查看MQ系统日志!");
        }
    }

}
