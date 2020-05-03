package com.zcoox.boot.elasticjob.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elastic-job 自动配置
 */
@Configuration
@ConditionalOnProperty("elastic-job.zookeeper.server-list")
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ElasticJobAutoConfig {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    /**
     * zookepper协调注册中心
     *
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zkCenter() {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(
                zookeeperProperties.getServerList(),
                zookeeperProperties.getNamespace());
        zkConfig.setBaseSleepTimeMilliseconds(zookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setMaxSleepTimeMilliseconds(zookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setConnectionTimeoutMilliseconds(zookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(zookeeperProperties.getSessionTimeoutMilliseconds());
        zkConfig.setMaxRetries(zookeeperProperties.getMaxRetries());
        zkConfig.setDigest(zookeeperProperties.getDigest());
        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    public ElasticJobInitializer elasticJobInitializer() {
        return new ElasticJobInitializer();
    }
}
