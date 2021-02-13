package org.example.chapter013.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.chapter013.processor.MsgProcessor;
import org.example.chapter013.protocol.IMMessage;

@Slf4j
public class TerminalServerHandler extends SimpleChannelInboundHandler<IMMessage> {

    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
        processor.sendMsg(ctx.channel(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Socket Client: 与客户端端口连接: " + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
