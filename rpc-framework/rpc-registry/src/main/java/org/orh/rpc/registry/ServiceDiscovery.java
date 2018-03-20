package org.orh.rpc.registry;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 服务发现
 */
@Component
public class ServiceDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

    @Value("${rpc.registry-address}")
    private String zkAddress;

    public String discover(String name) {
        // 创建 zookeeper 客户端
        ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_CONNECTION_TIMEOUT, Constant.ZK_SESSION_TIMEOUT);
        logger.info("connect to zookeeper");
        try {
            // 获取 service 节点
            String servicePath = Constant.ZK_REGISTRY_PATH + "/" + name;

            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }

            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtils.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }

            // 获取 address 节点
            String address;
            int size = addressList.size();
            if (size == 1) {
                // 若地址只有一个,则获取该地址
                address = addressList.get(0);
                logger.info("get only address node: {}", address);
            } else {
                // 若存在多个地址, 则随机获取一个地址
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                logger.info("get random address node: {}", address);
            }
            // 获取 address 节点的值
            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        } finally {
            zkClient.close();
        }
    }
}
