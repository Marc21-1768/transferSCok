package client;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.DimensionUIResource;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.awt.*;

public class Client {

    public static void main(String[] args) throws Exception{
        
        final File[] fileSend =new File[1];


        JFrame windows = new JFrame("CLIENT");
        windows.setLocationRelativeTo(null);
        windows.setSize(800,500);
        windows.setLayout(new BoxLayout(windows.getContentPane(),BoxLayout.Y_AXIS));
        windows.setDefaultCloseOperation(windows.EXIT_ON_CLOSE);


        JLabel file = new JLabel("LIST FILE");
        file.setBackground(Color.RED);
        file.setBorder(new EmptyBorder(10,5,10,0));
        file.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel bout = new JPanel();
        bout.setBackground(Color.BLACK);
        bout.setBorder(new EmptyBorder(75,0,10,0));


        JButton send = new JButton("SEND");
        send.setBackground(Color.GREEN);
        send.setPreferredSize(new Dimension(200,20));

        JButton select = new JButton("SELECTION FILE");
        select.setPreferredSize(new Dimension(200,20));

        bout.add(select);
        bout.add(send);

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JFileChooser fileChooser = new JFileChooser();
                
                if (fileChooser.showOpenDialog(null) == fileChooser.APPROVE_OPTION) {
                    fileSend[0] = fileChooser.getSelectedFile();
                    file.setText("File name: " + fileSend[0].getName());
                    
                }
            }
        });

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (fileSend[0] == null) {
                    file.setText("select a file");
                }
                else{
                try {
                FileInputStream fileInputStream = new FileInputStream(fileSend[0].getAbsoluteFile());
                Socket socket = new Socket("localhost",9999);
                
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                byte[] fileNameBytes = fileSend[0].getName().getBytes(); 
                byte[] sizeContent = new byte[(int)fileSend[0].length()];
                
                sizeContent = Files.readAllBytes(fileSend[0].toPath());

                dataOutputStream.writeInt(fileNameBytes.length);
                dataOutputStream.write(fileNameBytes);
                
                dataOutputStream.writeInt(sizeContent.length);
                dataOutputStream.write(sizeContent);
                    
            } catch (IOException error) {
                error.printStackTrace();
            }
            }}});

            windows.add(file);
            windows.add(bout);
            windows.setVisible(true);
            


    }

}
