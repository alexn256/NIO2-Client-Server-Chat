package engine.client;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client{

    private int port;
    private String host;
    private SocketChannel channel;
    private Socket socket;
    private InetSocketAddress address;

    public Client(String host, int port){
        try {
            address = new InetSocketAddress(host, port);
            channel = SocketChannel.open();
            channel.connect(address);
            channel.configureBlocking(false);
            while (true){
                write(consoleRead());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can't connected to " + address);
            System.exit(0);
        }

    }

    public String consoleRead(){
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public void write(String message){
        ByteBuffer buffer = ByteBuffer.allocate(message.getBytes().length);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()){
            try {
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost",9999);
    }
}
