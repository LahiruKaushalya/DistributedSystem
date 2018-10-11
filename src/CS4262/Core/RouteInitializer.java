package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import CS4262.Network.MessageHandler;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RouteInitializer {
    
    private final Node node;
    private final IDCreator idCreator;
    private final MainController mainController;
    private final MessageHandler msgHandler;
    
    public RouteInitializer(){
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.idCreator = new IDCreator();
        this.msgHandler = MessageHandler.getInstance();
    }
    
    public Node updateLocalRoutes(NodeDTO neighbour){
                
        Node[] routes = node.getRoutes();
        int m = idCreator.getBIN_ID_LENGTH();
        int bp = (int)Math.pow(2, m);
        
        String neighbourIP = neighbour.getIpAdress();
        int neighbourPort = neighbour.getPort();
        
        String neighbourID = idCreator.generateNodeID(neighbourIP, neighbourPort);
        int neighbourIntID = idCreator.getComparableID(neighbourID);
        int nodeID = idCreator.getComparableID(node.getId());
        
        int lowerbound = nodeID;
        int upperbound;
        Node temp = null;
        
        for (int i = m - 1; i >= 0; i--) {
            upperbound = (lowerbound + (int) Math.pow(2, i)) % bp;
            if (!isInRange(lowerbound, upperbound, neighbourIntID, bp)) {
                if (routes[i] == null) {
                    routes[i] = new Node(neighbourIP, neighbourPort, neighbourID);
                } 
                else {
                    int curID = idCreator.getComparableID(routes[i].getId());
                    if (Math.abs(curID - upperbound) > Math.abs(neighbourIntID - upperbound)) {
                        temp = routes[i];
                        routes[i] = new Node(neighbourIP, neighbourPort, neighbourID);
                    }
                    else{
                        temp = new Node(neighbourIP, neighbourPort, neighbourID);
                    }
                }
                break;
            }
        }
        //Set new routes
        node.setRoutes(routes);
        
        //Set node successor
        setNodeSuccessor( );
        
        return temp;
    }
    
    public void updateRoutesUI(){
        String displayText = "Index\tRange\tNode ID\tIP Address\tPort\n\n";
        
        Node[] routes = node.getRoutes();
        int nodeID = idCreator.getComparableID(node.getId());
        int m = idCreator.getBIN_ID_LENGTH();
        int bp = (int)Math.pow(2, m);
        
        String ip = "-", id = "-";
        int port = 0, rend, lbound = 0;
        NodeDTO tempNode;
        
        for(int i = 0; i < m; i++){
            tempNode = routes[i];
            if(tempNode != null){
                ip = tempNode.getIpAdress();
                port = tempNode.getPort();
                id = idCreator.generateNodeID(ip, port);
                
                for(int k = lbound; k <= i; k++){
                    rend = (nodeID + (int)Math.pow(2, k)) % bp;
                    displayText += k + "\t" +rend + "\t" + id + "\t" + ip + "\t" + port + "\n";
                }
                lbound = i + 1;
            }
            else{
                if(i == m - 1){
                    for (int k = lbound; k <= i; k++) {
                        rend = (nodeID + (int)Math.pow(2, k)) % bp;
                        displayText += k + "\t" + rend + "\t-\t-\t-\n";
                    }
                }
            }
        }
        mainController.getMainFrame().updateRoutingTable(displayText);
    }
    
    private boolean isInRange(int lowerbound, int upperbound, int value, int bp){
        if(upperbound < lowerbound){
            return (lowerbound <= value && value < bp) || (0 <= value && value < upperbound);
        }
        else{
            return (value < upperbound &&  lowerbound <= value );
        }
    }
    
    private void setNodeSuccessor(){
        
        Node[] routes = node.getRoutes();
        int m = idCreator.getBIN_ID_LENGTH();
        
        for(int i = 0; i < m; i++){
            Node temp = routes[i];
            if(temp != null){
                node.setSuccessor(new Node(
                        temp.getIpAdress(),
                        temp.getPort(),
                        idCreator.generateNodeID(temp.getIpAdress(), temp.getPort())
                ));
                break;
            }
        }
    }
    
}
