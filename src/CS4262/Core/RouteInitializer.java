package CS4262.Core;

import CS4262.Interfaces.IInitializerFileIndex;
import CS4262.Message.FileIndex.SendFileIndexToPre;
import CS4262.Message.Route.UpdatePredecessor;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Interfaces.IInitializerRoute;
import CS4262.Interfaces.IInitializerWordIndex;
import CS4262.Message.FileIndex.SendFileIndexToSuc;
import CS4262.Message.WordIndex.SendWordIndexToSuc;
import CS4262.Models.DataTransfer.MessageDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RouteInitializer implements IInitializerRoute, IInitializerFileIndex, IInitializerWordIndex{
    
    private final SendFileIndexToSuc sendFileIndexToSuc;
    private final SendWordIndexToSuc sendWordIndex;
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
        this.sendFileIndexToSuc = new SendFileIndexToSuc();
        this.sendWordIndex = new SendWordIndexToSuc();
    }
    
    public Node addAndUpdate(NodeDTO neighbour){
                
        Node[] routes = node.getRoutes();
        int m = idCreator.getBIN_ID_LENGTH();
        int bp = (int)Math.pow(2, m);
        
        String neighbourIP = neighbour.getipAdress();
        int neighbourPort = neighbour.getUdpPort();
        
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
        String id = idCreator.generateNodeID(leaver.getipAdress(), leaver.getUdpPort());
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
    
    private void setNodeSuccessor(){
        Node preSucc = node.getSuccessor();
        Node[] routes = node.getRoutes();
        int m = idCreator.getBIN_ID_LENGTH();
        node.setSuccessor(null);
        for(int i = 0; i < m; i++){
            Node temp = routes[i];
            if(temp != null){
                node.setSuccessor(new Node(
                        temp.getipAdress(),
                        temp.getUdpPort(),
                        idCreator.generateNodeID(temp.getipAdress(), temp.getUdpPort())
                ));
                break;
            }
        }
        Node newSucc = node.getSuccessor();
        boolean updated = false;
        if(newSucc != null){
            if(preSucc == null){
                updated = true;
            }
            else {
                if (!preSucc.getId().equals(newSucc.getId())) {
                    updated = true;
                }
            }
        }
        if(updated){
            updatePredecessor.send(new MessageDTO(newSucc));
            sendFileIndexToSuc.send(new MessageDTO(newSucc));
            sendWordIndex.send(new MessageDTO(newSucc));
            fileIndexInitializer.updateForSuccessor();
            wordIndexInitializer.updateForSuccessor();
            mainController.getMainFrame().updateSuccessorDetails(newSucc);
        }
    }
    
}
