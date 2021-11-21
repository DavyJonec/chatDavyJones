import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class Handler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>();
    private String clientName;
    private static int newClientIndex = 1;
    User user = new User();
    DatabaseHandler databaseHandler = new DatabaseHandler();





    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(user.getLogin());
        System.out.println("Client connected " + ctx);
        channels.add(ctx.channel());
        databaseHandler.getDbConnection();
        databaseHandler.getUser(user);
        newClientIndex ++;
        broadcastMessage("SERVER", "Connected new client: " + user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Client" + clientName + "disconnect");
        channels.remove(ctx.channel());
        broadcastMessage("SERVER", "Client" + clientName + "disconnected");
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Message received: " + s);
//        if(s.startsWith("/")) {
//            if(s.startsWith("/changename")) {
//                String newNickname = s.split("\\s", 2)[1];
//                broadcastMessage("SERVER",   clientName + " change nick on " + newNickname);
//                clientName = newNickname;
//            }
//            System.out.println("You changed name");
//            return;
//        }
        broadcastMessage(clientName, s);

    }
    public void broadcastMessage(String clientName, String message) {
        String out = String.format("[%s]: %s\n", clientName, message);
        for(Channel c : channels) {
            c.writeAndFlush(out);
        }
    }
}