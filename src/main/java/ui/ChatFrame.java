package ui;

import engine.client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
    private  JList userList;
    private Dimension dimension;
    private Client client;

    public ChatFrame(String username, String host){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();

        client = new Client(host, 9999, textArea, messageArea, username, userList);

        rootPanel.add(send);
        rootPanel.add(clear);
        rootPanel.add(textScrolling);
        rootPanel.add(messageScrolling);
        rootPanel.add(userList);
        add(rootPanel);

        Action sending = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                client.getWriter().readFromKeyboard();
                messageArea.setText("");
            }
        };

        send.addActionListener(e -> {client.getWriter().readFromKeyboard();
            messageArea.setText("");});

        messageArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "SEND");
        messageArea.getActionMap().put("SEND", sending);

        clear.addActionListener(e -> textArea.setText(""));

        dimension = new Dimension(600, 330);
        setIconImage(new ImageIcon("src/resources/logo.png").getImage());
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
        send.setFocusable(false);
        send.setBounds(300, 203 , 98, 35);
        send.setForeground(Color.GREEN);
        send.setIcon(new ImageIcon("src/resources/send.png"));

        clear = new JButton("Clear");
        clear.setFocusable(false);
        clear.setBounds(300,262 , 98, 35);
        clear.setForeground(Color.RED);
        clear.setIcon(new ImageIcon("src/resources/clear.png"));

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Calibri", Font.PLAIN, 12));

        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        Border panelBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);

        textScrolling = new JScrollPane(textArea);
        textScrolling.setBounds(0, 0, 400, 200);
        textScrolling.setBorder(panelBorder);
        messageArea.setFont(new Font("Calibri", Font.PLAIN, 12));

        messageScrolling = new JScrollPane(messageArea);
        messageScrolling.setBounds(0, 200, 300, 100);
        messageScrolling.setBorder(panelBorder);

        userList = new JList();
        userList.setBounds(400, 0, 200, 300);
        userList.setBorder(panelBorder);
    }
}
