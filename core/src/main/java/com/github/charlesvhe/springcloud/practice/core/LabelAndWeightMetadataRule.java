package com.github.charlesvhe.springcloud.practice.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by charles on 2017/5/22.
 */
public class LabelAndWeightMetadataRule extends ZoneAvoidanceRule {
    public static final String META_DATA_KEY_WEIGHT = "weight";
    public static final String META_DATA_KEY_VERSION = "version";

    private Random random = new Random();

    @Override
    public Server choose(Object key) {
        // 获取路由信息
        List<Server> serverList = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);
        if (CollectionUtils.isEmpty(serverList)) {
            return null;
        }

        // 如有路由信息，则取服务名
        String routeService = serverList.get(0).getMetaInfo().getServiceIdForDiscovery();
        System.out.println("**获取当前服务的版本号**");
        System.out.println("CoreHeaderInterceptor:" + CoreHeaderInterceptor.label.get());
        // 获取当前服务的版本号 FastJson
        JSONObject json = (JSONObject) JSONArray.parse(CoreHeaderInterceptor.label.get());
        String label = "";

        label = json.getString(routeService);
        System.out.println("当前请求版本号:" + label);

        // 计算总值并剔除0权重节点
        int totalWeight = 0;
        Map<Server, Integer> serverWeightMap = new HashMap<>();
        for (Server server : serverList) {
            Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();

            String serverVersion = metadata.get(META_DATA_KEY_VERSION);
            if (label.equals(serverVersion)) {
                String strWeight = metadata.get(META_DATA_KEY_WEIGHT);

                int weight = 100;
                try {
                    weight = Integer.parseInt(strWeight);
                } catch (Exception e) {
                    // 无需处理
                }

                if (weight <= 0) {
                    continue;
                }

                serverWeightMap.put(server, weight);
                totalWeight += weight;
            }
        }

        // 权重随机
        int randomWight = this.random.nextInt(totalWeight);
        int current = 0;
        for (Map.Entry<Server, Integer> entry : serverWeightMap.entrySet()) {
            current += entry.getValue();
            if (randomWight <= current) {
                return entry.getKey();
            }
        }

        return null;
    }
}
