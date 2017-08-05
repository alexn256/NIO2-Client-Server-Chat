package engine.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class ReadFromServer implements Runnable {

    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(256);

    public ReadFromServer(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        StringBuilder builder = new StringBuilder();
        int read;
        try {
            while (channel.isOpen()){
                while ((read = channel.read(buffer)) > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    builder.append(new String(bytes));
                    buffer.clear();
                }
                String message = builder.toString();
                builder.setLength(0);
                if (read < 0){
                    System.out.println("connection with server was terminated!");
                    channel.close();
                    System.exit(0);
                }
                else {
                    if (!message.equals("")){
                        System.out.println(message);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("reading data from server is impossible!");
        }
    }
}
