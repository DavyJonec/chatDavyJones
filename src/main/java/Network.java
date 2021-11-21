import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {
    private NioSocketChannel nioSocketChannel;

    private static final String HOST = "localhost";
    private static final int PORT = 12556;

    public Network(Callback onMessageReceivedCallback) {
        Thread t = new Thread(() -> {
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap client = new Bootstrap();
                client.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel channel) throws Exception {
                                nioSocketChannel = channel;
                                channel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ClientHandler(onMessageReceivedCallback));
                            }
                        });
                ChannelFuture future = client.connect(HOST, PORT).sync();
                System.out.println("Client connected");
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }

    public void sendMessage(String str) {
        nioSocketChannel.writeAndFlush(str);
    }

    public void close() {
        nioSocketChannel.close();
    }
}
