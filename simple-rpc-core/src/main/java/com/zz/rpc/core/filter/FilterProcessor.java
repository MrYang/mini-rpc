package com.zz.rpc.core.filter;

import com.zz.rpc.core.rpc.RpcRequest;
import com.zz.rpc.core.rpc.RpcResponse;
import com.zz.rpc.core.utils.ClassUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FilterProcessor {

    private static Filter beforeHead;
    private static Filter afterHead;

    private static List<Class<? extends Filter>> classList;

    static {
        classList = new ArrayList<>(2);
        classList.add(CallFilter.class);
        //classList.add(LogFilter.class);
    }

    static {
        List<Filter> list = new ArrayList<>();
        classList.forEach(clazz -> list.add(ClassUtils.newInstance(clazz)));
        setNext(list);
    }

    public static void before(RpcRequest request) {
        beforeHead.before(request);
        Filter rpcFilter = beforeHead.next();
        while (rpcFilter != null) {
            rpcFilter.before(request);
            rpcFilter = rpcFilter.next();
        }
    }

    public static void after(RpcResponse response) {
        afterHead.after(response);
        Filter rpcFilter = afterHead.previous();
        while (rpcFilter != null) {
            rpcFilter.after(response);
            rpcFilter = rpcFilter.previous();
        }
    }

    private static void setNext(List<Filter> list){
        Iterator<Filter> it = list.iterator();
        if (it.hasNext()) {
            beforeHead = it.next();
            Filter p = beforeHead;
            while (it.hasNext()) {
                Filter next = it.next();
                p.setNext(next);
                p = next;
            }
        }

        Collections.reverse(list);
        it = list.iterator();
        if (it.hasNext()) {
            afterHead = it.next();
            Filter p = afterHead;
            while (it.hasNext()) {
                Filter next = it.next();
                p.setPrevious(next);
                p = next;
            }
        }
    }
}
