package CS4262.Network;

import CS4262.MainFrame;
import CS4262.Models.DataTransfer.FileDTO;
import CS4262.Models.Node;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lahiru Kaushalya
 */
public class TCPServer extends Thread
{
    private final Node node;
    
    private static TCPServer instance; 
    
    public static TCPServer getInstance(Node node) {
        if(instance == null){
            instance = new TCPServer(node);
        }
        return instance;
    }
    
    private TCPServer(Node node){
        this.node = node;
    }
    
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startServer() throws IOException {
           this.start();
    }

    @Override
    public void run()
    {
        try {
            serverSocket = new ServerSocket(node.getTcpPort());
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String file = in.readLine();
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "file :"+file);
            FileDTO fileDto = extractRecievedFile(file);
            //JOptionPane.showMessageDialog(MainFrame.getInstance(), "Recieved file :"+fileDto.getFileObject().getName());
            stopServer();
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void stopServer() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    
    public FileDTO extractRecievedFile(String recievedFile)
    {
        FileDTO obj = null;
        // deserialize the object
        try {
            byte b[] = recievedFile.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = (FileDTO) si.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return obj;
    }
    
}
