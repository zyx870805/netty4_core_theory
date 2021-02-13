package org.example.chapter013.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.example.chapter013.processor.MsgProcessor;

@Slf4j
public class WebSocketClientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("client websocket msg: ", msg);
        //        processor.sendMsg(ctx.channel(), msg.text());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel client = ctx.channel();
        String addr = processor.getAddress(client);
        log.info("webSocket client: " + addr + "异常");
        cause.printStackTrace();
        ctx.close();
    }
}
