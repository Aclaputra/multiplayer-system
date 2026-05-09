package org.system;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class PlayerClient {
    public static void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class) // Datagram Channel for UDP
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() { // Handler for DatagramPackets
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                            // 3. Decode the incoming packet content
                            String s = packet.content().toString(CharsetUtil.UTF_8);
                            System.out.println("Server says: " + s);
                        }
                    });

            Channel channel = b.bind(0).sync().channel();

            InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8080);

            Scanner scanner = new Scanner(System.in);
            System.out.println("UDP Client started. Type a message:");

            while (true) {
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                ByteBuf buf = Unpooled.copiedBuffer(line, CharsetUtil.UTF_8);

                // Send the packet
                channel.writeAndFlush(new DatagramPacket(buf, serverAddress)).addListener(future -> {
                    if (!future.isSuccess()) {
                        System.err.println("Failed to send packet: " + future.cause());
                    }
                });
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
