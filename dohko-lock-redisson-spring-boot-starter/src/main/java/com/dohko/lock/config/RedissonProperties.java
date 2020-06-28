package com.dohko.lock.config;

import com.dohko.lock.ClusterTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 11:02
 */
@Data
@ConfigurationProperties(prefix = "dohko.redisson")
public class RedissonProperties {

    /**
     * 集群模式: single-单机；sentinel-哨兵；cluster-原生集群
     */
    private String clusterType = ClusterTypeEnum.SINGLE.getType();

    /**
     * redis地址
     * 格式：redis://127.0.0.1:6379
     */
    private String address = "redis://127.0.0.1:6379";

    /**
     * redis密码
     */
    private String password;

    /**
     * 使用redis哪个数据库
     */
    private int db = 0;

    /**
     * 哨兵模式的master name
     */
    private String sentinelMasterName;

    /**
     * 哨兵模式节点
     */
    private String[] sentinelNodes;

    /**
     * 集群模式节点
     */
    private String[] clusterNodes;

    /**
     * 连接超时时间
     */
    private int connectTimeout = 10000;

    /**
     * 连接池大小
     */
    private int connectionPoolSize = 64;
}
