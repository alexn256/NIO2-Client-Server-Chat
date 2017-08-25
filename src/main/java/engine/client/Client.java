package engine.client;


import engine.server.Server;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
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
    private JList list;
    private String host;
    private int port;
    private ArrayList<String> users = new ArrayList<>();
    final static Logger logger = Logger.getLogger(Client.class);

    public Client(String host, int port, JTextArea logArea, JTextArea messageArea, String username, JList list){
        this.host = host;
        this.port = port;
        this.logArea = logArea;
        this.messageArea = messageArea;
        this.list = list;
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
            Thread.sleep(500);
            users = list();
            writer = new WriteToServer(channel, user, messageArea);
            reader = new ReadFromServer(channel, logArea, list, users);
            service = Executors.newFixedThreadPool(2);
            service.execute(reader);
            service.execute(writer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The Server is not accessible or it may be down because of Network Issue", "ERROR", JOptionPane.ERROR_MESSAGE);
            logger.error("can't connected to " + address.getHostName(), e);
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
                logger.error("registration username on server is impassible!", e);
            }
        }
    }

    private ArrayList<String> list(){
        ArrayList<String> list = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        StringBuilder builder = new StringBuilder();
        try {
            while (channel.read(buffer) > 0){
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                builder.append(new String(bytes));
                buffer.clear();
            }
            String[] words = builder.toString().split("_");
            for(String usr : words){
                list.add(usr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
