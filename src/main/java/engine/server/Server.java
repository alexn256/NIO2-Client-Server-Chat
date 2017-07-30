package engine.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;


public class Server {

    private ServerSocketChannel serverChanel;
    private ServerSocket serverSocket;
    private int port;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private Selector selector;
    private InetSocketAddress socketAddress;

    public Server(int port) {
        socketAddress = new InetSocketAddress("localhost", port);
        try {
            serverChanel = ServerSocketChannel.open();
            serverChanel.bind(socketAddress);
            serverChanel.configureBlocking(false);
           new Thread(new Readble(serverChanel)).start();
        } catch (IOException e) {
            System.err.println("impossible open server socket channel!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9999);
    }
}
