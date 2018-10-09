package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RouteInitializer {
    
    private final MainController mainController;
    private final Node node;
    private final IDCreator idCreator;
    
    public RouteInitializer(){
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.idCreator = new IDCreator();
    }
    
    public void updateRoutes(NodeDTO neighbour){
                
        Node[] routes = node.getRoutes();
        int m = routes.length;
        int bp = (int)Math.pow(2, m);
        
        String neighbourIP = neighbour.getIpAdress();
        int neighbourPort = neighbour.getPort();
        
        String neighbourID = idCreator.generateNodeID(neighbourIP, neighbourPort);
        int neighbourIntID = idCreator.getComparableID(neighbourID);
        int nodeID = idCreator.getComparableID(node.getId());
        
        if(neighbourIntID == nodeID + 1 || (neighbourIntID == 0 && nodeID == bp - 1)){
            routes[0] = new Node(neighbourIP, neighbourPort, neighbourID);
        }
        else{
            int lowerbound = nodeID;
            int upperbound;
            for (int i = m - 1; i >= 0; i--) {
                upperbound = (lowerbound + (int) Math.pow(2, i)) % bp;
                if (!isInRange(lowerbound, upperbound, neighbourIntID, bp)) {
                    if (i != m - 1) {
                        if (routes[i + 1] == null) {
                            routes[i + 1] = new Node(neighbourIP, neighbourPort, neighbourID);
                        } 
                        else {
                            int curID = idCreator.getComparableID(routes[i + 1].getId());
                            int preUpbound = (upperbound + (int) Math.pow(2, i + 1)) % bp;
                            
                            if(Math.abs(preUpbound - curID) > Math.abs(preUpbound - neighbourIntID)){
                                routes[i + 1] = new Node(neighbourIP, neighbourPort, neighbourID);
                            }
                        }
                        break;
                    } 
                    else {
                        break;
                    }
                }
            }
        }
        // Update routing table UI
        String displayText = "Index\tRange\tNode ID\tIP Address\tPort\n\n";
        NodeDTO temp;
        String ip;
        int port, rend, rstart = nodeID, start = nodeID;
        
        for(int i = 0; i < m; i++){
            rend = (start + (int)Math.pow(2, i)) % bp;
            displayText += i + "\t" +rstart + " - " + rend + "\t";
            temp = routes[i];
            if(temp != null){
                ip = temp.getIpAdress();
                port = temp.getPort();
                displayText += idCreator.generateNodeID(ip, port) + "\t" + ip + "\t" + port + "\n";
            }
            else{
                displayText += "-\t-\t-\t\n";
            }
            rstart = rend;
        }
        mainController.getMainFrame().updateRoutingTable(displayText);
    }
    
    private boolean isInRange(int lowerbound, int upperbound, int value, int bp){
        if(upperbound < lowerbound){
            return (lowerbound < value && value < bp) || (0 <= value && value <= upperbound);
        }
        else{
            return (value <= upperbound &&  lowerbound < value );
        }
    }
    
}
