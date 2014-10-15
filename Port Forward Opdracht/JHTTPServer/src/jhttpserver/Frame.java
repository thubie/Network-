package jhttpserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener {
    JLabel lblStatus;

    public Frame() {
        this.setTitle("Simple Sample");
        this.setSize(320, 240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lblStatus = new JLabel();
        lblStatus.setText("server not started");
        lblStatus.setBounds(50, 70, 230, 21);
        add(lblStatus);

        this.setVisible(true);
    }

//    public static void main(String[] args) {
//        new Frame();
//        JHTTP.main(args);
//    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    
    public void writeLabel(String text){
    	lblStatus.setText(text);
    }

  

}
