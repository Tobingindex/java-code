package top.tobing.netty_in_action.ch4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * $ 属性值的使用
 *
 * @author tobing
 * @date 2022/2/15 22:48
 * @description
 */
public class ChannelOptionUsing {
    public static void main(String[] args) {
        // 创建一个AttributeKey标识该属性
        AttributeKey<Integer> id = AttributeKey.valueOf("ID");
        Bootstrap bootstrap = new Bootstrap();
        // 设置EventLoopGroup，其提供了用以处理Channel事件的EventLoop
        bootstrap.group(new NioEventLoopGroup())
                // 指定Channel的实现
                .channel(NioSocketChannel.class)
                // 设置用以处理Channel的I/O以及数据的ChannelInboundHandle
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                             @Override
                             public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                 // 使用AttributeKey检索属性以及它的值
                                 Integer idValue = ctx.channel().attr(id).get();
                                 // do something with the idValue
                             }

                             @Override
                             protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                 System.out.println("Received data");
                             }
                         }
                );
        // 设置ChannelOption，其将在connect()或者bind()方法被调用时被设置到已经创建的Channel上
        bootstrap
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        // 储存该id属性
        bootstrap.attr(id, 123456);
        // 使用配置好的Bootstrap实例连接到远程的主机
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }

}
