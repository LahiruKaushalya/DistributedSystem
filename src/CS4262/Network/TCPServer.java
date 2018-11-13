package CS4262.Network;

import CS4262.Models.Node;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class TCPServer implements Runnable{
    
    private final Node node;
    private final Thread thread;
    
    private static TCPServer instance; 
    
    public static TCPServer getInstance(Node node) {
        if(instance == null){
            instance = new TCPServer(node);
        }
        return instance;
    }
    
    private TCPServer(Node node){
        this.node = node;
        this.thread = new Thread(this);
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Socket connectionSocket = new ServerSocket(node.getTcpPort()).accept();
                BufferedReader inFromClient
                        = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                String recievedMessage = inFromClient.readLine();
                
                System.out.println("Received: " + recievedMessage);
                
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
