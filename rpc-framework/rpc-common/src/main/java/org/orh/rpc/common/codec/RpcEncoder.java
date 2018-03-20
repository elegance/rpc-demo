package org.orh.rpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.orh.rpc.common.util.SerializationUtil;

/**
 * RPC 编码器
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in); //序列化
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
