package org.example.chapter013.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.chapter013.protocol.IMDecoder;
import org.example.chapter013.protocol.IMEncoder;
import org.example.chapter013.server.hander.HttpServerHandler;
import org.example.chapter013.server.hander.TerminalServerHandler;
import org.example.chapter013.server.hander.WebSocketServerHandler;

@Slf4j
public class ChatServer {
    private int port = 80;

    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new IMDecoder());
                            pipeline.addLast(new IMEncoder());
                            pipeline.addLast(new TerminalServerHandler());

                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(64*1024));
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpServerHandler());

                            pipeline.addLast(new WebSocketServerProtocolHandler("/in"));
                            pipeline.addLast(new WebSocketServerHandler());
                        }
                    });
            ChannelFuture future = b.bind(this.port).sync();
            log.info("服务已启动，监听端口" + this.port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void start() {
        start(this.port);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            new ChatServer().start(Integer.valueOf(args[0]));
        } else {
            new ChatServer().start();
        }
    }

}
