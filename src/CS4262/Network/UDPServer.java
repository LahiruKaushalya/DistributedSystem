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
public class UDPServer implements Runnable {
    
    private final Node node;
    private final Thread thread;
    private DatagramSocket server;
    private static UDPServer instance; 
    
    public static UDPServer getInstance(Node node) {
        if(instance == null){
            instance = new UDPServer(node);
        }
        return instance;
    }
    
    private UDPServer(Node node){
        this.node = node;
        this.thread = new Thread(this);
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Server starts on port " + node.getUdpPort());
            server = new DatagramSocket(node.getUdpPort());
            server.setSoTimeout(500);
            while(true){
                try {
                    byte[] buff = new byte[65536];
                    DatagramPacket incoming = new DatagramPacket(buff, buff.length);
                    server.receive(incoming);
                    
                    //Create a new ClientHandler thread object 
                    Thread ch = new MessageReceiver(incoming, server);
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
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
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

