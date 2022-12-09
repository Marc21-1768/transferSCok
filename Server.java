package server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseListener;
import javax.swing.plaf.DimensionUIResource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import server.Fichier;

public class Server {
    static ArrayList<Fichier> fileS = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        
        int ID =0;

        JFrame windows = new JFrame("Serveur");
        windows.setBackground(Color.BLUE);
        windows.setSize( 400,350);
        windows.setLocationRelativeTo(null);
        windows.setLayout(new BoxLayout(windows.getContentPane(), BoxLayout.Y_AXIS));
        windows.setDefaultCloseOperation(windows.EXIT_ON_CLOSE);
        windows.setVisible(true);


        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        JLabel file = new JLabel(" file received select the file");
        file.setPreferredSize(null);
        file.setBorder(new EmptyBorder(20,0,10,0));
        file.setAlignmentX(Component.CENTER_ALIGNMENT);

        windows.add(file);
        windows.add(pan);
        windows.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(9999);
        
        while(true){
            try {
                
                Socket sckt = serverSocket.accept();

                DataInputStream dataInStr = new DataInputStream(sckt.getInputStream());

                int fileNameLength = dataInStr.readInt();

                if (fileNameLength>0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInStr.readFully(fileNameBytes,0,fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = dataInStr.readInt();
                    if (fileContentLength>0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInStr.readFully(fileContentBytes,0,fileContentLength);

                        JPanel lignes = new JPanel();
                        lignes.setLayout(new BoxLayout(lignes, BoxLayout.Y_AXIS));

                        JLabel nomFichier = new JLabel(fileName);
                        nomFichier.setBorder(new EmptyBorder(10,0,10,0));

                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            lignes.setName(String.valueOf(ID));
                            lignes.addMouseListener(getMyMouseListener());
                            lignes.add(nomFichier);
                            pan.add(lignes);
                            windows.validate();
                        }
                        else{
                            lignes.setName(String.valueOf(ID));
                            lignes.addMouseListener(getMyMouseListener());
                            lignes.add(nomFichier);
                            pan.add(lignes);

                            windows.validate();
                        }
                        fileS.add(new Fichier(ID, fileName, fileContentBytes, getFileExtension(fileName)));
                    }   
                }
            } catch (IOException error) {
                error.printStackTrace();
            }
        }


    }

    public static MouseListener getMyMouseListener(){

            return new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent event) {

                    JPanel jPanel = (JPanel)event.getSource();
                     
                    int id = Integer.parseInt(jPanel.getName());

                    for (Fichier leFichier : fileS) {
                        if(leFichier.getId() == id){
                            JFrame window = creatFrame(leFichier.getNom(),leFichier.getDonnee(),leFichier.getFichierExtens());
                            window.setVisible(true);
                        }
                    }

                }
                @Override
                public void mousePressed(MouseEvent event) {}
                @Override
                public void mouseReleased(MouseEvent event) {}
                @Override
                public void mouseEntered(MouseEvent event) {}
                @Override
                public void mouseExited(MouseEvent event) {}
            };
    }

    public static JFrame creatFrame(String nom,byte[] donnee,String fichierExtens){
        JFrame windows = new JFrame();
        windows.setLocationRelativeTo(null);
        windows.setSize(600,500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("DOWNLOAD FILE");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(20,0,10,0));

        JButton save = new JButton("DOWNLOAD");
        save.setBackground(Color.GREEN);
        save.setPreferredSize(new Dimension(120,40));

        JButton no = new JButton("CANCEL");
        no.setPreferredSize(new Dimension(120,40));

        JLabel fichierDown = new JLabel();
        fichierDown.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btns = new JPanel();
        btns.setBackground(Color.black);
        btns.setBorder(new EmptyBorder(20,10,10,0));
        btns.add(save);
        btns.add(no);

        if (fichierExtens.equalsIgnoreCase("txt")) {
            fichierDown.setText("<html>" + new String(donnee)+ "</html>");
        }
        else{
            fichierDown.setIcon(new ImageIcon(donnee));
        }

        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionE) {
                File downFile = new File(nom);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(downFile);
                    fileOutputStream.write(donnee);
                    fileOutputStream.close();
                    windows.dispose();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
            
        });

        no.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionE) {
                windows.dispose();
            }
        });

        panel.add(label);
        panel.add(fichierDown);
        panel.add(btns);

        windows.add(panel);
        return windows;

    }

    public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if(i>0){
            return fileName.substring(i+1);
        }
        return "NO FILE SELECTED";
    }

}
