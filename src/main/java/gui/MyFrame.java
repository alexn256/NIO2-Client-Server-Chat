package gui;

import engine.client.Client;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private Client client;
    private JPanel rootPanel;
    private JPanel southPanel;
    private JButton send;
    private JTextField textField;
    private JTextArea area;

    MyFrame(){
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        rootPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new BorderLayout());
        rootPanel.add(southPanel, BorderLayout.SOUTH);
        textField = new JTextField(30);
        send = new JButton("send");
        area = new JTextArea();
        rootPanel.add(area , BorderLayout.CENTER);
        southPanel.add(textField, BorderLayout.WEST);
        southPanel.add(send, BorderLayout.EAST);
        add(rootPanel);

        setVisible(true);


    }

    public static void main(String[] args) {
        MyFrame myFrame = new MyFrame();
    }
}
