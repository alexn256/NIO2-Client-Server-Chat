package engine.client;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;


public class ReadFromServer implements Runnable {

    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private JTextArea area;
    private JList list;
    private DefaultListModel model;
    private ArrayList<String> users;


    public ReadFromServer(SocketChannel channel, JTextArea area, JList list, ArrayList<String> users) {
        this.channel = channel;
        this.area = area;
        this.list = list;
        this.users = users;
        setUsers();
    }

    @Override
    public void run() {
        StringBuilder builder = new StringBuilder();
        int read;
        try {
            while (channel.isOpen()) {
                while ((read = channel.read(buffer)) > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    builder.append(new String(bytes));
                    buffer.clear();
                }
                String message = builder.toString();
                builder.setLength(0);
                if (read < 0) {
                    System.out.println("connection with server was terminated!");
                    channel.close();
                    System.exit(0);
                } else {
                    if (!message.equals("")) {
                        if (ListChanged(message)){
                            String[] word = message.split(",");
                            if (word[word.length - 1].equals(" connected to us...")){
                                if (!users.contains(word[0])){
                                    users.add(word[0]);
                                    setUsers();
                                }
                            }else {
                                if (users.contains(word[0])){
                                    users.remove(word[0]);
                                    setUsers();
                                }
                            }
                        }
                        System.out.println(message);
                        if (area.getText().length() < 1) {
                            area.setText(message);
                            builder.setLength(0);
                        } else {
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

    private boolean ListChanged(String message) {
        String[] words = message.split(",");
        if(words.length > 1){
            return  (words[1].equals(" connected to us...") || words[1].equals(" has left us."));
        }
        return false;
    }

    private void setUsers(){
        Collections.sort(users);
        model = new DefaultListModel();
        for (String user : users) {
            model.addElement(user);
        }
        list.setModel(model);
    }
}
