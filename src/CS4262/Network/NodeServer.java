package CS4262.Network;

import CS4262.Models.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
    private DatagramSocket server;
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
            server = new DatagramSocket(node.getPort());
            server.setSoTimeout(500);
            while(true){
                try {
                    byte[] buff = new byte[65536];
                    DatagramPacket incoming = new DatagramPacket(buff, buff.length);
                    server.receive(incoming);
                    
                    //Create a new ClientHandler thread object 
                    Thread ch = new ClientHandler(incoming, server);
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
            server.close();
            instance = null;
            System.out.println("Server stopped...");
        }
    }
    
    public void stopServer(){
        thread.interrupt();
    }
    
    public void startServer(){
        thread.start();
    }
}

