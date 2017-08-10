package engine.client;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client{

    private User user;
    private ByteBuffer buffer = ByteBuffer.allocate(64);
    private SocketChannel channel;
    private InetSocketAddress address;
    private ExecutorService service;
    private WriteToServer writer;
    private ReadFromServer reader;
    private JTextArea messageArea;
    private JTextArea logArea;
    private String host;
    private int port;
    private String username;

    public Client(String host, int port, JTextArea logArea, JTextArea messageArea, String username){
        this.host = host;
        this.port = port;
        this.logArea = logArea;
        this.messageArea = messageArea;
        user = new User(username);
        start();
    }

    private void start(){
        try {
            address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);
            channel.configureBlocking(false);
            writeUsername(user.getName());
            writer = new WriteToServer(channel, user, messageArea);
            reader = new ReadFromServer(channel, logArea);
            service = Executors.newFixedThreadPool(2);
            service.execute(reader);
            service.execute(writer);
            greeting(user.getName());
        } catch (IOException e) {
            System.out.println("can't connected to " + address.getHostName());
            System.exit(0);
        }
    }

    public WriteToServer getWriter() {
        return writer;
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

    private void greeting( String name){
        ByteBuffer buffer = ByteBuffer.wrap((name+" connected to us...").getBytes());
        try {
            channel.write(buffer);
        } catch (IOException e) {
            System.out.println("broadcast greeting is impossible!");
        }
    }
}
