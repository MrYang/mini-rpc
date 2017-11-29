package com.zz.rpc.core.cluster.impl;

import com.zz.rpc.core.cluster.Cluster;
import com.zz.rpc.core.cluster.HaStrategy;
import com.zz.rpc.core.cluster.LoadBalance;

/**
 * @author Mr.Yang
 * @since 2017-11-29
 */
public class DefaultCluster implements Cluster {

    private LoadBalance loadBalance;
    private HaStrategy haStrategy;


}
