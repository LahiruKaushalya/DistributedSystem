package CS4262;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public void initializeNode(ArrayList<NodeDTO> nodes) {

        if (nodes != null) {
            int nodeID = idCreator.getComparableID(node.getId());
            int neighbourintID;
            int successorID = nodeID;
            int predecessorID = nodeID;
            String neighbourID;

            for (NodeDTO neighbour : nodes) {
                neighbourID = idCreator.generateNodeID(neighbour.getIpAdress(), neighbour.getPort());
                neighbourintID = idCreator.getComparableID(neighbourID);
                Node newNode = new Node(neighbour.getIpAdress(), neighbour.getPort(), "", neighbourID);
                
                if (nodeID < neighbourintID) {
                    if(nodeID == successorID){
                        successorID = neighbourintID;
                        node.setSuccessor(newNode);
                    }
                    else{
                        if(neighbourintID < successorID){
                            successorID = neighbourintID;
                            node.setSuccessor(newNode);
                        }
                    }
                } 
                else if (neighbourintID < nodeID) {
                    if(nodeID == predecessorID){
                        predecessorID = neighbourintID;
                        node.setPredecessor(newNode);
                    }
                    else{
                        if( predecessorID < neighbourintID){
                            predecessorID = neighbourintID;
                            node.setPredecessor(newNode);
                        }
                    }
                }
            }
            //Send Join message Update UI
            if(node.getSuccessor() != null){
                msgHandler.join(node.getSuccessor());
                mc.getMainFrame().updateSuccessorDetails(node.getSuccessor());
            }
            if(node.getPredecessor() != null){
                msgHandler.join(node.getPredecessor());
                mc.getMainFrame().updatePredecessorDetails(node.getPredecessor());
            }
        }
    }
    
}
