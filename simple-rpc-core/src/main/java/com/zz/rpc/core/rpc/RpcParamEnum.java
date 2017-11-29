package com.zz.rpc.core.rpc;

import com.zz.rpc.core.constant.Constants;

/**
 * @author Mr.Yang
 * @since 2017-11-28
 */
public enum RpcParamEnum {

    version("version", Constants.DEFAULT_VERSION),
    group("group", Constants.DEFAULT_GROUP);

    private String name;
    private String value;

    private RpcParamEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
