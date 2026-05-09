package org.system;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Set<InetSocketAddress> players = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        InetSocketAddress sender = packet.sender();
        String msg = packet.content().toString();

        ByteBuf buf = packet.content();
        String message = buf.toString(CharsetUtil.UTF_8);

        if (!players.contains(sender)) {
            players.add(sender);
            broadcast(ctx, "[SERVER] - Player " + sender + " has joined!", null);
        }

        broadcast(ctx, message, sender);
    }

    private void broadcast(ChannelHandlerContext ctx, String message, InetSocketAddress sender) {
        ByteBuf baseBuf = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);

        try {
            for (InetSocketAddress address : players) {
                String prefix = "";
                if (sender != null) {
                    prefix = address.equals(sender) ? "[You] " : "[" + sender.getAddress().getHostAddress() + ":" + sender.getPort() + "] ";
                }

                ByteBuf packetBuf = Unpooled.copiedBuffer(prefix + message, CharsetUtil.UTF_8);

                ctx.write(new DatagramPacket(packetBuf, address));
            }

            ctx.flush();

        } finally {
            baseBuf.release();
        }
    }
}
