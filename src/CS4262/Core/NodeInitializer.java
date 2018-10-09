package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import CS4262.Network.MessageHandler;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.util.ArrayList;

/**
 *
 * @author Lahiru Kaushalya
 */
//This will recieve all of the available nodes in the bootstrap server
public class NodeInitializer {

    private final Node node;
    private final MainController mc;
    private final IDCreator idCreator;
    private final MessageHandler msgHandler;

    public NodeInitializer() {
        this.mc = MainController.getInstance();
        this.msgHandler = MessageHandler.getInstance();
        this.node = mc.getNode();
        this.idCreator = new IDCreator();
    }

    /**
     *
     * @param nodes
     */
    public void initializeNode(ArrayList<NodeDTO> neighbours) {
        String response;
        if (neighbours != null) {
            for(NodeDTO neighbour : neighbours){
                response = msgHandler.join(neighbour);
                System.out.println(response);
            }
        }
    }
    
}
