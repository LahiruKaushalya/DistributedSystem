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

    public NodeInitializer() {
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.msgHandler = MessageHandler.getInstance();
        this.routeInitializer = new RouteInitializer();
    }

    /**
     *
     * @param neighbours
     */
    public void initializeNode(ArrayList<NodeDTO> neighbours) {
        String response;
        if (neighbours != null) {
            for(NodeDTO neighbour : neighbours){
                response = msgHandler.join(neighbour);
                routeInitializer.updateLocalRoutes(neighbour);
                /*
                Handle response here
                */
            }
            routeInitializer.updateRoutesUI();
        }
        
        Node successor = node.getSuccessor();
        if(successor != null){
            response = msgHandler.updateRoutes(successor, node, null);
            /*
            Handle response here
            */
        }
    }
    
}
