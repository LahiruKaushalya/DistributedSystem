package CS4262.Core;

import CS4262.Helpers.Messages.SendFileIndex;
import CS4262.Helpers.Messages.UpdatePredecessor;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RouteInitializer implements Initializer{
    
    private final SendFileIndex sendFileIndex;
    private final UpdatePredecessor updatePredecessor;
    
    private static RouteInitializer instance;
    
    public static RouteInitializer getInstance(){
        if(instance == null){
            instance = new RouteInitializer();
        }
        return instance;
    }
    
    private RouteInitializer(){
        this.updatePredecessor = new UpdatePredecessor();
        this.sendFileIndex = new SendFileIndex();
    }
    
    public Node addAndUpdate(NodeDTO neighbour){
                
        Node[] routes = node.getRoutes();
        int m = idCreator.getBIN_ID_LENGTH();
        int bp = (int)Math.pow(2, m);
        
        String neighbourIP = neighbour.getIpAdress();
        int neighbourPort = neighbour.getPort();
        
        String neighbourID = idCreator.generateNodeID(neighbourIP, neighbourPort);
        int neighbourIntID = idCreator.getComparableID(neighbourID);
        int nodeID = idCreator.getComparableID(node.getId());
        Node newNode = new Node(neighbourIP, neighbourPort, neighbourID);
       
        int upperbound;
        Node temp = null;
        
        for (int i = m - 1; i >= 0; i--) {
            upperbound = (nodeID + (int) Math.pow(2, i)) % bp;
            if(rangeChecker.isInRange(nodeID, upperbound, neighbourIntID)) {
                if (routes[i] == null) {
                    routes[i] = newNode;
                } 
                else {
                    int curID = idCreator.getComparableID(routes[i].getId());
                    if (rangeChecker.isInRange(curID, upperbound, neighbourIntID)) {
                        temp = routes[i];
                        routes[i] = newNode;
                    }
                    else{
                        temp = newNode;
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
    
    public void removeAndUpdate(NodeDTO leaver){
        String id = idCreator.generateNodeID(leaver.getIpAdress(), leaver.getPort());
        Node[] routes = node.getRoutes();
        
        for(int i = 0; i < routes.length; i++){
            if(routes[i] != null){
                if (routes[i].getId().equals(id)) {
                    routes[i] = null;
                }
            }
        }
        //Set new routes
        node.setRoutes(routes);
        
        //Set node successor
        setNodeSuccessor();
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
        mainController.getMainFrame().updateSuccessorDetails(node.getSuccessor());
        mainController.getMainFrame().updateRoutingTable(displayText);
    }
    
    private void setNodeSuccessor(){
        Node preSucc = node.getSuccessor();
        Node[] routes = node.getRoutes();
        int m = idCreator.getBIN_ID_LENGTH();
        node.setSuccessor(null);
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
        Node newSucc = node.getSuccessor();
        if(preSucc == null && newSucc != null){
            updatePredecessor.send(newSucc);
            sendFileIndex.send(newSucc);
            mainController.getMainFrame().updateSuccessorDetails(newSucc);
        }
        else if(preSucc != null && newSucc != null){
            if(!preSucc.getId().equals(newSucc.getId())){
                updatePredecessor.send(newSucc);
                sendFileIndex.send(newSucc);
                mainController.getMainFrame().updateSuccessorDetails(newSucc);
            }
        }
    }
    
}
