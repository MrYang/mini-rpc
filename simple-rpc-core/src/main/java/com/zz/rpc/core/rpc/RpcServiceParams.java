package com.zz.rpc.core.rpc;

import org.springframework.util.StringUtils;

import static com.zz.rpc.core.constant.Constants.DEFAULT_GROUP;
import static com.zz.rpc.core.constant.Constants.DEFAULT_SPLIT;
import static com.zz.rpc.core.constant.Constants.DEFAULT_VERSION;

/**
 * @author Mr.Yang
 * @since 2017-11-28
 */
public class RpcServiceParams {

    private String version;
    private String interfaceName;
    private String group;

    public RpcServiceParams(String version, String interfaceName, String group) {
        this.version = version;
        this.interfaceName = interfaceName;
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public static String key(String version, String group, String interfaceName) {
        version = StringUtils.isEmpty(version) ? DEFAULT_VERSION : version;
        group = StringUtils.isEmpty(group) ? DEFAULT_GROUP : group;
        return group + DEFAULT_SPLIT + interfaceName + DEFAULT_SPLIT + version;
    }
}
