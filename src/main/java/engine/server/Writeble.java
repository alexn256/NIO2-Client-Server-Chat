package engine.server;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class Writeble implements Runnable{
    
    private ServerSocketChannel channel;
    private Selector selector;
    private ByteBuffer buffer;

    public Writeble(ServerSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            channel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        while (true){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isWritable()){
                    writeAll();
                }
            }
        }
    }

    private void writeAll() {
    }
}
