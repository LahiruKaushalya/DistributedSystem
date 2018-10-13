package CS4262.Network;

import CS4262.Models.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class NodeServer implements Runnable {
    
    private final Node node;
    private final Thread thread;
    private ServerSocket server;
    private static NodeServer instance; 
    
    public static NodeServer getInstance(Node node) {
        if(instance == null){
            instance = new NodeServer(node);
        }
        return instance;
    }
    
    private NodeServer(Node node){
        this.node = node;
        this.thread = new Thread(this);
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Server starts on port " + node.getPort());
            server = new ServerSocket(node.getPort());
            server.setSoTimeout(500);
            while(true){
                try {
                    //Socket object to receive incoming node requests
                    Socket socket = server.accept();
                    
                    //Obtaining input and out streams 
                    DataInputStream inStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
                    
                    //Create a new ClientHandler thread object 
                    Thread ch = new ClientHandler(node, socket, inStream, outStream);
                    ch.start();
                }
                catch(SocketTimeoutException ex){
                    //Check for unregistration
                    if(thread.isInterrupted()){
                        break;
                    }
                }
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(NodeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                server.close();
                instance = null;
                System.out.println("Server stopped.");
            } 
            catch (IOException ex) {
                Logger.getLogger(NodeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void stopServer(){
        thread.interrupt();
    }
    
    public void startServer(){
        thread.start();
    }
}

