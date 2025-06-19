package com.ruoyi.factory.service.impl;

import com.ruoyi.factory.service.ISysPlcService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ISysPlcServiceImpl implements ISysPlcService {

    @Override
    public Map<String, Object> subscriptionPlc()  throws Exception {
        String endpointUrl = "opc.tcp://192.168.1.100:4840"; // 替换为实际的 PLC IP 和端口
        OpcUaClient client = null;
        try {
            client = OpcUaClient.create(
                    endpointUrl,
                    endpoints -> endpoints.stream()
                            .filter(e -> e.getSecurityPolicyUri().equals("http://opcfoundation.org/UA/SecurityPolicy#None"))
                            .findFirst(),
                    configBuilder -> configBuilder
                            .setApplicationName(LocalizedText.english("Java OPC UA Client"))
                            .setApplicationUri("urn:example:client")
                            .setIdentityProvider(new AnonymousProvider())
                            .setRequestTimeout(UInteger.valueOf(5000))
                            .build()
            );

            client.connect().get(); // 连接到 OPC UA 服务器
            createSubscription(client);
        } catch (Exception e) {
//            if (null != client){
//                //断开连接
//                client.disconnect().get();
//            }
            e.printStackTrace();
        }
        return null;
    }


    private void createSubscription(OpcUaClient client) throws Exception {
        // 创建订阅
        double publishingInterval = 1000.0; // 发布间隔，单位为毫秒
        UaSubscription subscription = client.getSubscriptionManager().createSubscription(publishingInterval).get();
        // 创建要订阅的节点
        NodeId nodeId = new NodeId(2, "Demo.Static.Scalar.Double"); // 替换为实际的节点 ID
        ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);

        // 创建监控参数
        MonitoringParameters parameters = new MonitoringParameters(
                UInteger.valueOf(1),
                1000.0, // 采样间隔
                null, // 过滤器
                UInteger.valueOf(10), // 队列大小
                true // 丢弃旧值
        );

        // 创建监控项请求
        MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting, parameters);
        List<MonitoredItemCreateRequest> requests = new ArrayList<>();
        requests.add(request);

        // 创建监控项并注册回调函数
        List<UaMonitoredItem> items = subscription.createMonitoredItems(
                TimestampsToReturn.Both,
                requests,
                (item, id) -> {
                    item.setValueConsumer((it, value) -> {
                        System.out.println("监控到Node ID: " + it.getReadValueId().getNodeId());
                        System.out.println("监控到Value: " + value.getValue().getValue());
                    });
                }
        ).get();
    }
}
