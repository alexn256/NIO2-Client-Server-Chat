package engine.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.concurrent.*;


public class Server {

    private ServerSocketChannel serverChanel;
    private ServerSocket serverSocket;
    private int port;
    private Selector selector;
    private InetSocketAddress socketAddress;
    private ExecutorService service;

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
            System.err.println("impossible open server socket channel!");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Server server = new Server(9999);
    }
}
