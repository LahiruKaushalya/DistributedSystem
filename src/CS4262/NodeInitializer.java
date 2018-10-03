package CS4262;

import java.util.ArrayList;

/**
 *
 * @author Lahiru Kaushalya
 */

//This will recieve all of the available nodes in the bootstrap server

public class NodeInitializer {
    
    private final String ipAddress;
    private final int port;
    private final MainController mc;
    
    public NodeInitializer(){
        this.mc = new MainController();
        this.ipAddress = mc.getIpAddress();
        this.port = mc.getPort();
    }
    
    /**
     * 
     * @param nodes 
     */
    public void initializeNode(ArrayList<ArrayList<String>> nodes) 
    {
        System.out.print(nodes);
    }
}
