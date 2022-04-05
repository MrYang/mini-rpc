package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;
import com.zz.rpc.core.utils.ClassUtils;

import java.util.*;

public class FilterProcessor {

    private static LinkedList<Filter> filters = new LinkedList<>();

    static {
        List<Class<? extends Filter>> classList = new LinkedList<>();
        classList.add(CallFilter.class);
        classList.add(LogFilter.class);

        classList.forEach(clazz -> filters.add(ClassUtils.newInstance(clazz)));
    }

    public static void before(RpcRequest request) {
        filters.forEach(filter -> filter.before(request));
    }

    public static void after(RpcResponse response) {
        Iterator<Filter> iterator = filters.descendingIterator();
        while (iterator.hasNext()) {
            iterator.next().after(response);
        }
    }
}
