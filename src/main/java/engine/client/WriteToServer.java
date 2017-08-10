package engine.client;

import engine.message.Message;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteToServer implements Runnable {

    private SocketChannel channel;
    private User user;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private JTextArea area;
    private String message;

    public WriteToServer(SocketChannel channel, User user, JTextArea area) {
        this.channel = channel;
        this.user = user;
        this.area = area;
    }

    @Override
    public void run() {
        try {
            while (channel.isOpen()){
               if (message != null && !message.equals("")){
                   buffer.clear();
                   buffer.put(message.getBytes());
                   buffer.flip();
                   while (buffer.hasRemaining()){
                       channel.write(buffer);
                   }
                   message = "";
               }
            }
        } catch (IOException e) {
            System.out.println("can not send data to the server!");
        }
    }

    public void readFromKeyboard() {
        Message string = new Message(area.getText(), user);
        message = string.getMessage();
    }
}
