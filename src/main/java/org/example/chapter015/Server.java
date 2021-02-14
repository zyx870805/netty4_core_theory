package org.example.chapter015;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Server {
    public static final int BEGIN_PORT = 8000;
    public static final int N_PORT = 8100;

    public static void main(String[] args) throws FileNotFoundException {
        System.setOut(new PrintStream(new File("sever.txt")));
        new Server().start(Server.BEGIN_PORT, Server.N_PORT);
    }

    public void start(int beginPort, int endPort) {
        System.out.println("服务端启动中。。。");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        bootstrap.childHandler(new ConnectionCountHandler());

        for(int i = 0; i <= (endPort - beginPort); i ++) {
            final int port = beginPort + i;

            bootstrap.bind(port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("成功绑定监听端口: " + port);
                }
            });
        }
        System.out.println("服务端已启动!");
    }
}
