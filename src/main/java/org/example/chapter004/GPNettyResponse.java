package org.example.chapter004;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

public class GPNettyResponse {

    private ChannelHandlerContext context;

    private HttpRequest req;

    public GPNettyResponse(ChannelHandlerContext context, HttpRequest req) {
        this.context = context;
        this.req = req;
    }

    public void write(String out) throws Exception {
        try {
            if (out == null || out.length() == 0) {
                return;
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

            response.headers().set("Content-Type", "text/html");

            context.write(response);
        } finally {
            context.flush();
            context.close();
        }
    }
}
