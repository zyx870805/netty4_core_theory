package org.example.chapter013.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.chapter013.protocol.IMMessage;
import org.example.chapter013.protocol.IMP;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ChatClientHandler extends SimpleChannelInboundHandler<IMMessage> {

    private ChannelHandlerContext ctx;
    private String nickName;
    public ChatClientHandler(String nickName) {
        this.nickName = nickName;
    }

    private void session() throws IOException {
        new Thread() {
            @Override
            public void run() {
                System.out.println(nickName + ",你好，请在看控制台输入对话内容");
                IMMessage message = null;
                Scanner scanner = new Scanner(System.in);
                do {
                    if (scanner.hasNext()) {
                        String input = scanner.nextLine();
                        if ("exit".equals(input)) {
                            message = new IMMessage(IMP.LOGOUT.getName(), "Console", System.currentTimeMillis(), nickName);
                        } else {
                            message = new IMMessage(IMP.CHAT.getName(), System.currentTimeMillis(), nickName,input);
                        }
                    }
                }while(sendMsg(message));
                scanner.close();
            }
        }.start();
    }

    private boolean sendMsg(IMMessage msg) {
        ctx.channel().writeAndFlush(msg);
        System.out.println("继续输入开始对话...");
        return msg.getCmd().equals(IMP.LOGOUT) ? false : true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        IMMessage message = new IMMessage(IMP.LOGIN.getName(), "Console", System.currentTimeMillis(), this.nickName);
        sendMsg(message);
        log.info("成功连接服务器，已执行登录动作");
        session();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
        IMMessage m = msg;
        System.out.println((null == m.getSender() ? "" : (m.getSender() + ":")) + removeHtmlTag(m.getContent()));
    }

    public static String removeHtmlTag(String htmlStr) {
        String regEx_script = "<script[^>]*?[\\s\\S]*?<\\/script>";
        String regEx_style = "style[^>]*?>[\\s\\S]*?<\\/style>";
        String regEx_html = "<[^>]+>";

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");

        return htmlStr.trim();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("与服务器端口连接:" + cause.getMessage());
        cause.printStackTrace();
        ctx.channel();
    }
}
