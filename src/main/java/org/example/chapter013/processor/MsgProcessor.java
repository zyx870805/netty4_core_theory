package org.example.chapter013.processor;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.example.chapter013.protocol.IMDecoder;
import org.example.chapter013.protocol.IMEncoder;
import org.example.chapter013.protocol.IMMessage;
import org.example.chapter013.protocol.IMP;

public class MsgProcessor {

    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    public static final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    public static final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");
    public static final AttributeKey<String> FROM = AttributeKey.valueOf("from");

    private IMDecoder decoder = new IMDecoder();
    private IMEncoder encoder = new IMEncoder();

    public String getNickName(Channel client) {
        return client.attr(NICK_NAME).get();
    }

    public String getAddress(Channel client) {
        return client.remoteAddress().toString().replaceFirst("/", "");
    }

    public JSONObject getAttrs(Channel client) {
        try {
            return client.attr(ATTRS).get();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setAttrs(Channel client, String key, Object value) {
        try {
            JSONObject json = client.attr(ATTRS).get();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key, value);
            client.attr(ATTRS).set(jsonObject);
        }
    }

    public void logout(Channel client) {
        if (getNickName(client) == null) {
            return;
        }

        for(Channel channel : onlineUsers) {
            IMMessage request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickName(client) + "离开");
            String content = encoder.encode(request);
            channel.writeAndFlush(new TextWebSocketFrame(content));
        }
        onlineUsers.remove(client);
    }

    public void sendMsg(Channel client, IMMessage msg) {
        sendMsg(client, encoder.encode(msg));
    }

    public void sendMsg(Channel client, String msg) {
        IMMessage request = decoder.decode(msg);
        if (null == request) {
            return;
        }

        String addr = getAddress(client);

        if (request.getCmd().equals(IMP.LOGIN.getName())) {
            client.attr(NICK_NAME).getAndSet(request.getSender());
            client.attr(IP_ADDR).getAndSet(addr);
            client.attr(FROM).getAndSet(request.getTerminal());
            onlineUsers.add(client);

            for(Channel channel : onlineUsers) {
                boolean isself = (channel == client);
                if (!isself) {
                    request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickName(client) + "加入");
                } else {
                    request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "已与服务器建立链接");
                }

                if ("Console".equals(channel.attr(FROM).get())) {
                    channel.writeAndFlush(request);
                    continue;
                }
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }else if (request.getCmd().equals(IMP.CHAT.getName())) {
            for (Channel channel : onlineUsers) {
                boolean isself = (channel == client);
                if (isself) {
                    request.setSender("you");
                } else {
                    request.setSender(getNickName(client));
                }
                request.setTime(sysTime());
                if ("Console".equals(channel.attr(FROM).get()) & !isself) {
                    channel.writeAndFlush(request);
                    continue;
                }
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        } else if(request.getCmd().equals(IMP.FLOWER.getName())) {
            JSONObject attrs = getAttrs(client);
            long currTime = sysTime();
            if (null != attrs) {
                long lastTime = attrs.getLongValue("lastFlowTime");
                int seconds = 10;
                long sub = currTime - lastTime;
                if (sub < 1000 * seconds) {
                    request.setSender("you");
                    request.setCmd(IMP.SYSTEM.getName());
                    request.setContent("您发送鲜花太频繁," + (seconds - Math.round(sub/1000)) + "秒后重试");

                    String content = encoder.encode(request);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }

            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                    request.setContent("你给大家送了一遍鲜花雨");
                    setAttrs(client, "lastFlowTime", currTime);
                } else {
                    request.setSender(getNickName(client));
                    request.setContent(getNickName(client) + "送来一波鲜花雨");
                }
                request.setTime(sysTime());

                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
    }

    private Long sysTime() {
        return System.currentTimeMillis();
    }

}
