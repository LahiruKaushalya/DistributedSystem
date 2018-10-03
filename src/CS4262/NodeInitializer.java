package CS4262;

import java.util.ArrayList;

/**
 *
 * @author Lahiru Kaushalya
 */

//This will recieve all of the available nodes in the bootstrap server

public class NodeInitializer {
    
    private final Node node;
    private final MainController mc;
    
    public NodeInitializer(){
        this.mc = new MainController();
        this.node = mc.getNode();
    }
    
    /**
     * 
     * @param nodes 
     */
    public void initializeNode(ArrayList<Node> nodes) 
    {
        System.out.print(nodes);
    }
}
