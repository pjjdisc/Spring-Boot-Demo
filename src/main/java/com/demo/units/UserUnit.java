package com.demo.units;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:25
 */
public class UserUnit {
    public static Long getUserId(){
        RpcContext context = RpcContext.getContext();
        return (Long) context.get("userId");
    }
}
