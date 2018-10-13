package CS4262.Core;

import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Network.MessageHandler;
import CS4262.Models.NodeDTO;

import java.util.ArrayList;

/**
 *
 * @author Lahiru Kaushalya
 */
//This will recieve 2 random nodes in the bootstrap server
public class NodeInitializer {
    
    private final Node node;
    private final MessageHandler msgHandler;
    private final MainController mainController;
    private final RouteInitializer routeInitializer;
    private static int hopCount;
    
    public NodeInitializer() {
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.msgHandler = MessageHandler.getInstance();
        this.routeInitializer = new RouteInitializer();
        NodeInitializer.hopCount = 5;
    }

    public static int getHopCount() {
        return hopCount;
    }

    /**
     *
     * @param newNodes
     */
    public void initializeNode(ArrayList<NodeDTO> newNodes) {
        String response;
        if (newNodes != null) {
            for(NodeDTO neighbour : newNodes){
                response = msgHandler.join(neighbour);
                routeInitializer.addAndUpdate(neighbour);
                /*
                Handle response here
                */
            }
            routeInitializer.updateRoutesUI();
        }
        
        Node[] neighbours = node.getRoutes();
        for(Node neighbour : neighbours){
            if(neighbour != null){
                response = msgHandler.updateRoutes(neighbour, node, null, hopCount);
                /*
                Handle response here
                */
            }
        }
    }
    
}
