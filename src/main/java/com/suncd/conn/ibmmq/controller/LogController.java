/*
成都太阳高科技有限责任公司
http://www.suncd.com
*/
package com.suncd.conn.ibmmq.controller;

import com.suncd.conn.ibmmq.utils.FileUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mq/log")
public class LogController {

    @RequestMapping(value = "/current",method = RequestMethod.GET)
    public String current(){
        return FileUtil.readLastNLineString(new File("logs/mq/sysLog/out_2018-10-05.0.log"),40L);
    }

    @RequestMapping(value = "/send/files",method = RequestMethod.GET)
    public List<Map<String ,String >> sendLogFiles(){
        return FileUtil.getFiles(new File("logs/mq/sysLog"));
    }

}
