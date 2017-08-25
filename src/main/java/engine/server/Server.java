package engine.server;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.*;



public class Server {

    private ServerSocketChannel serverChanel;
    private Selector selector;
    private InetSocketAddress socketAddress;
    private ExecutorService service;
    final static Logger logger = Logger.getLogger(Server.class);

    public Server(int port) {
        socketAddress = new InetSocketAddress("localhost", port);
        try {
            serverChanel = ServerSocketChannel.open();
            serverChanel.bind(socketAddress);
            serverChanel.configureBlocking(false);
            selector = Selector.open();
            service = Executors.newFixedThreadPool(2);
            service.execute(new Readable(serverChanel));
        } catch (IOException e) {
            logger.error("impossible open server socket channel!", e);
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Server server = new Server(9999);
    }
}
