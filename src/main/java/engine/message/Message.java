package engine.message;


import engine.client.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String text;
    private User user;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public Message(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public String getMessage(){
        if (text.length() > 0){
            return sdf.format(new Date()) + " " + user.getName() + ": " + text;
        }
        else return "";
    }
}
