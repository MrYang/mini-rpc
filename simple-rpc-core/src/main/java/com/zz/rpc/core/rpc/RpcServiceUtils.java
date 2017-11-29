package com.zz.rpc.core.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Yang
 * @since 2017-11-28
 */
public class RpcServiceUtils {

    public static Map<String, Object> serviceMap = new ConcurrentHashMap<>();
}
