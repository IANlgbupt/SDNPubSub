package edu.bupt.wangfu.module.managerMgr.networkVirtualization.POJO;

import lombok.Data;



/*
[
 {
   "name" : <string>,
   "dpid" : <dpid>,
   "priority" : <number>,
   "match" : <match-struct>,
   "queues" : [ <queue_id> ],
            OPTIONAL
   "force-enqueue" : <queue_id>,
            OPTIONAL
   "slice-action" :
   [
    {
      "slice-name" : <name> ",
      "permission" : <perm-value>
    }
   ]
 }
]
 */
@Data
public class FlowReq {
    public String name;
    public String dpid;
    public int priority;
    public String match;
    public QueueInfo queue;
}
