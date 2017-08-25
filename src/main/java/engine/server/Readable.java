package engine.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;


public class Readable implements Runnable {

    private ServerSocketChannel channel;
    private Selector selector;
    private ByteBuffer buffer;
    private ArrayList<String> users = new ArrayList<>();
    final static Logger logger = Logger.getLogger(Readable.class);

    public Readable(ServerSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            logger.error("selector can not be opened!", e);
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
                logger.error("to get a selection keys is impossible!", e);
            }
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
            String message = builder.toString();
            if (read < 0){
                message = key.attachment() + ", has left us.";
                users.remove(key.attachment());
                logger.info(message);
                socketChannel.close();
                broadcast(message);
            }
            else {
                if (key.attachment() == null && !message.equals("")){
                    key.attach(message);
                    users.add(message);
                    sendUsers(key);
                    Thread.sleep(1000);
                    broadcast(message + ", connected to us...");
                }
                else {
                    logger.info(message);
                    broadcast(message);
                }
            }
        } catch (IOException e) {
            logger.error("connection with client was terminated...", e);
        } catch (InterruptedException e) {
            logger.error("connection with client was terminated...", e);
        }
    }

    private void accept(SelectionKey key) {
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            String address = socketChannel.socket().getInetAddress().toString();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, key.OP_READ);
            logger.info("successfully connection from:" + address);
        } catch (IOException e) {
            logger.error("connection to the client can not be established!!!", e);
        }
    }

    private void broadcast(String message){
        buffer = ByteBuffer.wrap(message.getBytes());
        try {
            for (SelectionKey key : selector.keys()) {
                if (key.isValid() && key.channel() instanceof SocketChannel) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.write(buffer);
                    buffer.rewind();
                }
            }
        }
        catch (IOException e){
            logger.error("can not write message to clients", e);
        }
    }

    private void sendUsers(SelectionKey key){
        try {
            StringBuilder builder = new StringBuilder();
            for (String user : users) {
                builder.append(user+ "_");
            }
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.wrap(builder.toString().getBytes());
            channel.write(buffer);
        } catch (IOException e) {
            logger.error("clients can not be sent through the channel", e);
        }
    }
}
