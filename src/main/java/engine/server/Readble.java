package engine.server;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Readble implements Runnable {

    private Selector selector;
    private ServerSocketChannel channel;
    private ByteBuffer buffer;

    public Readble(ServerSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            String address = (new StringBuilder(socketChannel.socket().getInetAddress().toString())).append(":").append(socketChannel.socket().getPort()).toString();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, key.OP_READ);
            System.out.println("successfully connection from:" + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        StringBuilder builder = new StringBuilder();
        buffer = ByteBuffer.allocate(256);
        int read = 0;
        try {
            while ((read = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                builder.append(new String(bytes));
                buffer.clear();
            }
            String message;
            if (read < 0) {
                message = key.attachment() + " left the chat.\n";
                socketChannel.close();
            } else {
                message = key.attachment() + ": " + builder.toString();
            }
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
