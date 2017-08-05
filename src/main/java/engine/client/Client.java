package engine.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client{

    private User user = new User("Test User");
    private ByteBuffer buffer = ByteBuffer.allocate(64);
    private SocketChannel channel;
    private InetSocketAddress address;
    private ExecutorService service;

    public Client(String host, int port){
        try {
            address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);
            channel.configureBlocking(false);
            writeUsername(user.getName());
            service = Executors.newFixedThreadPool(2);
            service.execute(new ReadFromServer(channel));
            service.execute(new WriteToServer(channel, user));
        } catch (IOException e) {
            System.out.println("can't connected to " + address.getHostName());
            System.exit(0);
        }
    }

    private void writeUsername(String username){
        buffer.clear();
        buffer.put(username.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()){
            try {
                channel.write(buffer);
            } catch (IOException e) {
                System.out.println("registration username on server is impassible!");
            }
        }
    }

    private void close(){
        service.shutdown();
        System.exit(0);
    }

    public static void main(String[] args){
        Client client = new Client("localhost",9999);
    }
}
