package ui;

import engine.client.Client;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;


public class ChatFrame extends JFrame{

    private JPanel rootPanel;
    private JTextArea messageArea;
    private JTextArea textArea;
    private JScrollPane textScrolling;
    private JScrollPane messageScrolling;
    private JButton send;
    private JButton clear;
    private Dimension dimension;

    private Client client;

    public ChatFrame(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dimension = new Dimension(400, 300);

        init();
        rootPanel.add(send);
        rootPanel.add(clear);
        rootPanel.add(textScrolling);
        rootPanel.add(messageScrolling);
        add(rootPanel);

        client = new Client("localhost", 9999, textArea, messageArea);

        send.addActionListener(e -> {client.getWriter().readFromKeyboard();
        messageArea.setText("");});

        ImageIcon logo = new ImageIcon("logo.png");
        setIconImage(logo.getImage());

        setTitle("NIO2 Chat v1.0.1");
        setMinimumSize(dimension);
        setSize(dimension);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init(){
        rootPanel = new JPanel();
        rootPanel.setLayout(null);

        send = new JButton("Send");
        send.setBounds(300, 203 , 98, 35);

        clear = new JButton("Clear");
        clear.setBounds(300,262 , 98, 35);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        Border panelBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);

        textScrolling = new JScrollPane(textArea);
        textScrolling.setBounds(0, 0, 400, 200);
        textScrolling.setBorder(panelBorder);
        messageArea.setFont(new Font("Source Code Pro", Font.PLAIN, 16));

        messageScrolling = new JScrollPane(messageArea);
        messageScrolling.setBounds(0, 200, 300, 100);
        messageScrolling.setBorder(panelBorder);
    }

    public static void main(String[] args) {
        new ChatFrame();
    }
}
