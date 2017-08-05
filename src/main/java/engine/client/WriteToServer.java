package engine.client;

import engine.message.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteToServer implements Runnable {

    private SocketChannel channel;
    private BufferedReader keyboardReader;
    private User user;
    private ByteBuffer buffer = ByteBuffer.allocate(256);

    public WriteToServer(SocketChannel channel, User user) {
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));
        this.channel = channel;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            while (channel.isOpen()){
                String message = readFromKeyboard();
                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                while (buffer.hasRemaining()){
                    channel.write(buffer);
                }
            }
        } catch (IOException e) {
            System.out.println("can not send data to the server!");
        }
    }

    private String readFromKeyboard() throws IOException {
        Message message = new Message(keyboardReader.readLine(), user);
        return message.getMessage();
    }
}
