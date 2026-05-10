package org.system;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import io.netty.channel.socket.DatagramPacket;

import java.awt.*;
import java.net.InetSocketAddress;

public class PlayerClient {
    private static InetSocketAddress serverAddress;
    private static Channel channel;
    private static EventLoopGroup group;
    public static void start() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class) // Datagram Channel for UDP
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() { // Handler for DatagramPackets
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                            String s = packet.content().toString(CharsetUtil.UTF_8);
                            System.out.println("Server says: " + s);
                        }
                    });

            channel = b.bind(0).sync().channel();

            serverAddress = new InetSocketAddress("localhost", 8080);

            System.out.println("UDP Client started.");

//            channel.closeFuture().await(); // go to lobby without awaiting,
        } catch (InterruptedException e) {
            group.shutdownGracefully();
            throw new RuntimeException(e);
        }
    }

    public static void stop() {
        try {
            if (channel != null && channel.isOpen()) {
                sendMessage("DISCONNECTED from Server");
                channel.close().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (group != null) {
                group.shutdownGracefully();
            }
        }
    }

    public static void sendMessage(String message) {
        if (channel != null && channel.isActive()) {
            ByteBuf buf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
            channel.writeAndFlush(new DatagramPacket(buf, serverAddress));
            System.out.println("Sent to server: " + message);
        } else {
            System.err.println("Cannot send: Channel is not active.");
        }
    }
}
