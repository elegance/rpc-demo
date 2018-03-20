package org.orh.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.orh.grpc.server.annotation.GrpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GrpcServer implements ApplicationContextAware, InitializingBean {

    private List<BindableService> serviceBeanList = new ArrayList<>();

    @Value("${grpc.port}")
    private int port;

    // 来自 ApplicationContextAware, 可以在方法中获取 spring 的 ApplicationContext对象，从而获取带有 @GrpcService 注解的 RPC 接口实现类实例；
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(GrpcService.class);
        if (serviceBeanMap != null) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof BindableService) {
                    serviceBeanList.add((BindableService) serviceBean);
                }
            }
        }
    }

    // 来自 initializingBean ，可以在该方法中绑定 RPC 端口号，并添加所有 RPC 接口实现类实例，最后启动 RPC 服务器
    public void afterPropertiesSet() throws Exception {
        ServerBuilder builder = ServerBuilder.forPort(port);
        for (BindableService serviceBean : serviceBeanList) {
            builder.addService(serviceBean);
        }
        Server server = builder.build().start();
        System.out.println("server started, listening on " + port);
        server.awaitTermination();
    }

}
