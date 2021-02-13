package org.example.chapter013.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class IMEncoder extends MessageToByteEncoder<IMMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new MessagePack().write(msg));
    }

    public String encode(IMMessage msg) {
        if (null == msg) {
            return "";
        }
        String prefix = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
        if (IMP.LOGIN.getName().equals(msg.getCmd()) || IMP.FLOWER.getName().equals(msg.getCmd())) {
            prefix += ("[" + msg.getSender() + "][" + msg.getTerminal() + "]");
        } else if (IMP.CHAT.getName().equals(msg.getCmd())) {
            prefix += ("[" + msg.getSender() + "]");
        } else if(IMP.SYSTEM.getName().equals(msg.getCmd())) {
            prefix +=("[" + msg.getOnline() + "]");
        }
        if (!(null == msg.getContent() || "".equals(msg.getContent()))) {
            prefix += (" - " + msg.getContent());
        }
        return prefix;
    }
}
