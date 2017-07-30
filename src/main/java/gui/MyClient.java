package gui;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class MyClient {
    private Socket socket;
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    private boolean reading;

    MyClient() throws Exception {
        Scanner scanner = new Scanner(System.in);
        socket = new Socket("localhost", 1488);
        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());

        System.out.println("connection successful!");


        //Thread thread = new Thread(new MyCallable(fromServer));
        //thread.start();

        String massege = "";
        while (!massege.equals("exit")) {
            massege = scanner.nextLine();
            toServer.writeUTF(massege);
            toServer.flush();

        }
    }



    public static void main(String[] args) throws Exception {
        MyClient myClient = new MyClient();
    }

}

class MyCallable implements Callable<String>{
    private DataInputStream fromServer;
    private boolean reading;

    public MyCallable(DataInputStream fromServer) {
        this.fromServer = fromServer;
    }

    @Override
    public String call() throws Exception {
        String message = "";
        try {
            while (reading == false) {
                message = fromServer.readUTF();
                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public void setReading() {
        reading = true;
    }
}
