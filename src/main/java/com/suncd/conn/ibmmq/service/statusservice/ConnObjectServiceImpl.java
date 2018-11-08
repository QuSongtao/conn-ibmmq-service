package com.suncd.conn.ibmmq.service.statusservice;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.suncd.conn.ibmmq.dao.ConnConfObjectDao;
import com.suncd.conn.ibmmq.entity.ConnConfObject;
import com.suncd.conn.ibmmq.utils.PageResponse;
import com.suncd.conn.ibmmq.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnObjectServiceImpl implements ConnObjectService {
    @Autowired
    private ConnConfObjectDao connConfObjectDao;
    @Autowired
    private QmgrService qmgrService;

    @Override
    public Response getObjByType(String objType, String transferType, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        Page<ConnConfObject> connConfObjects = (Page<ConnConfObject>) connConfObjectDao.selectByType(objType, transferType);

        List<ConnConfObject> connConfObjectList = connConfObjects.getResult();
        for (ConnConfObject connConfObject : connConfObjectList) {
            if (objType.equals("CHANNEL")) {
                connConfObject.setObjStatus(chlStatus(qmgrService.getChannelStatus(connConfObject.getObjName())));
            } else if (objType.equals("QUEUE")) {
                connConfObject.setObjStatus("" + qmgrService.getLocalQueueDepth(connConfObject.getObjName()));
            }
        }
        PageResponse<ConnConfObject> pageResponse = new PageResponse<>(connConfObjects.getTotal(), connConfObjectList);
        return new Response<>().success(pageResponse);
    }

    /**
     * 通道状态翻译
     * <p>
     * 0-正在初始化
     * 1-正在绑定
     * 3-正在运行
     * 5-正在重试
     * 6-已停止
     * -1-未知状态
     *
     * @param code 状态码
     * @return 状态
     */
    private String chlStatus(int code) {
        String retStr;
        switch (code) {
            case 0:
                retStr = "正在初始化";
                break;
            case 1:
                retStr = "正在绑定";
                break;
            case 3:
                retStr = "正在运行";
                break;
            case 5:
                retStr = "正在重试";
                break;
            case 6:
                retStr = "已停止";
                break;
            default:
                retStr = "未知状态";
                break;
        }
        return retStr;
    }

}
