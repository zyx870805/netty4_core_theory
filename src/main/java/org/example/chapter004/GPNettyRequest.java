package org.example.chapter004;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class GPNettyRequest {

    private ChannelHandlerContext context;

    private HttpRequest req;

    public GPNettyRequest(ChannelHandlerContext ctx, HttpRequest req) {
        this.context = ctx;
        this.req = req;
    }

    public String getUrl() {
        return req.getUri();
    }

    public String getMethod() {
        return req.method().name();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
        return decoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> parameters = getParameters();
        List<String> param = parameters.get(name);
        if (null == param) {
            return null;
        } else {
            return param.get(0);
        }
    }
}
