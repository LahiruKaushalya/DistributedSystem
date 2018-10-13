package CS4262.Helpers;

import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RouteHandler extends MsgHandler{
    
    public RouteHandler(){
        super();
    }
    
    @Override
    public void handle(StringTokenizer st) {
        
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        hopCount--;
        
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        //Number of routes
        int routes = Integer.parseInt(st.nextToken());
        
        //Collect nodes that does not include/replaced by a new node in routing table
        ArrayList<Node> addi = new ArrayList<>();
        NodeDTO temp;
        
        //Update routing table 
        if(routes > 1){
            for(int i = 0; i < routes; i++){
                temp = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
                Node tempN;
                if(i != 0){
                    tempN = routeInitializer.addAndUpdate(temp);
                    if(tempN != null){
                        addi.add(tempN);
                    }
                }
            }
            routeInitializer.updateRoutesUI();
        }
        
        String id = node.getId();
        String senderID = idCreator.generateNodeID(senderIP, senderPort);
        
        //Check whether it is own msg and not expire
        if(!id.equals(senderID) && hopCount > 0){
            Node[] neighbours = node.getRoutes();
            //Routes can have null values
            for(Node neighbour : neighbours){
                if (neighbour != null) {
                    //pass update message to all neighbours in routing table 
                    msgSender.updateRoutes(neighbour, sender, addi, hopCount);
                }
            }
        }
    }
    
}
