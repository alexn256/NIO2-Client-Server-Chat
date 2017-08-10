package engine.client;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class ReadFromServer implements Runnable {

    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private JTextArea area;

    public ReadFromServer(SocketChannel channel, JTextArea area) {
        this.channel = channel;
        this.area = area;
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
                        if (area.getText().length() < 1){
                            area.setText(message);
                            builder.setLength(0);
                        }
                        else {
                            builder.append(area.getText() + "\n" + message);
                            area.setText(builder.toString());
                            builder.setLength(0);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("reading data from server is impossible!");
        }
    }
}
