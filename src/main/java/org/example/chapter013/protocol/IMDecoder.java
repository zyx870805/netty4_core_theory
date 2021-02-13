package org.example.chapter013.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDecoder extends ByteToMessageDecoder {

    private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        try {
            final int length = in.readableBytes();
            final byte[] array = new byte[length];
            String content = new String(array, in.readerIndex(), length);
            //
            if (!(null == content || "".equals(content.trim()))) {
                if (!IMP.isIMP(content)) {
                    ctx.channel().pipeline().remove(this);
                    return;
                }
            }

            in.getBytes(in.readerIndex(), array, 0, length);
            out.add(new MessagePack().read(array, IMMessage.class));
            in.clear();
        }catch (MessageTypeException e) {
            ctx.channel().pipeline().remove(this);
        }
    }

    public IMMessage decode(String msg) {
        if (null == msg || "".equals(msg.trim())) {
            return null;
        }

        try {
            Matcher m = pattern.matcher(msg);
            String header = "";
            String content = "";
            if (m.matches()) {
                header = m.group(1);
                content = m.group(3);
            }

            String[] headers = header.split("\\]\\[");
            long time = 0;
            try {
                time = Long.parseLong(headers[1]);
            }catch(Exception e) {
                e.printStackTrace();
            }
            String nickName = headers[2];
            nickName = nickName.length() < 10 ? nickName : nickName.substring(0, 9);

            if (msg.startsWith("["+IMP.LOGIN.getName()+"]")) {
                return new IMMessage(headers[0], headers[3], time, nickName);
            } else if(msg.startsWith("["+IMP.CHAT.getName()+"]")) {
                return new IMMessage(headers[0], time, nickName, content);
            } else if(msg.startsWith("[" + IMP.FLOWER.getName() + "]")) {
                return new IMMessage(headers[0], headers[3], time, nickName);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
