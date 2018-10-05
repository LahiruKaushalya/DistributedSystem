package CS4262;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class NodeServer extends Thread {
    
    private final Node node;
    
    private Socket socket; 
    private ServerSocket server; 
    private DataInputStream inStream; 
    
    
    public NodeServer(Node node){
        this.node = node; 
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Server starts on port " + node.getPort());
            server = new ServerSocket(node.getPort());
            socket = server.accept();
            inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
            String data;
            
            while(true){
                data = inStream.readUTF(); 
                System.out.println(data);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(NodeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
