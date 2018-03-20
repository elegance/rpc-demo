package org.orh.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.orh.rpc.common.bean.RpcRequest;
import org.orh.rpc.common.bean.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    /**
     * 存放服务名称与服务实例之间的映射关系
     */
    private final Map<String, Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        // 创建 RPC 响应对象
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequstId());

        Object result = hande(rpcRequest);
        response.setResult(result);
    }

    private Object hande(RpcRequest rpcRequest) throws Exception {
        // 获取服务实例
        String serviceName = rpcRequest.getInterfaceName();
        Object serviceBean = handlerMap.get(serviceName);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
        }

        // 获取反射调用所需的变量
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        // 执行反射调用
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}
