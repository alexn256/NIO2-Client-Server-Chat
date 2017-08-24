package ui;

import javax.swing.*;


public class LoginFrame extends JFrame{

    private JPanel rootPanel;
    private JLabel host;
    private JLabel username;
    private JTextField hostField;
    private JTextField usernameField;
    private JButton button;

    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();

        rootPanel.add(host);
        rootPanel.add(username);
        rootPanel.add(hostField);
        rootPanel.add(usernameField);
        rootPanel.add(button);
        add(rootPanel);

        ImageIcon logo = new ImageIcon("src/resources/logo.png");
        setIconImage(logo.getImage());

        setTitle("NIO2 Chat v1.0.1");
        setSize(190,275);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents(){
        rootPanel = new JPanel();
        rootPanel.setLayout(null);

        hostField = new JTextField();
        hostField.setBounds(20, 50, 145, 25);
        hostField.setColumns(10);

        usernameField = new JTextField();
        usernameField.setBounds(20, 125, 145, 25);
        usernameField.setColumns(10);

        host = new JLabel("Server Host", SwingConstants.CENTER);
        host.setBounds(45, 25, 100, 20);

        username = new JLabel("Username", SwingConstants.CENTER);
        username.setBounds(45, 100, 100, 20);

        button = new JButton("Login");
        button.setBounds(50, 175, 80, 30 );

        button.addActionListener(e -> {new ChatFrame(usernameField.getText(), hostField.getText());
            setVisible(false);});
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}