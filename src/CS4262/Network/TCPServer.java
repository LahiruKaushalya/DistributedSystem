package CS4262.Network;

import CS4262.Models.Node;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
