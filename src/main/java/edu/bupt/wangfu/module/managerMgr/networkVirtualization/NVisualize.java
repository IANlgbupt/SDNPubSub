package edu.bupt.wangfu.module.managerMgr.networkVirtualization;


import edu.bupt.wangfu.module.managerMgr.networkVirtualization.POJO.FlowReq;
import edu.bupt.wangfu.module.managerMgr.networkVirtualization.POJO.sliceReq;

public class NVisualize {
    //flowvisor api的http调用 json rpc api
    public static boolean addSlice(sliceReq req){
        return true;
    }
    public static boolean updateSlice(sliceReq req){
        return true;
    }
    public static boolean removeSlice(String sliceName){
        return true;
    }
    public static boolean addFlow(FlowReq req){
        return true;
    }
}
