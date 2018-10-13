package CS4262.Helpers;

import CS4262.Core.NodeInitializer;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class LeaveHandler extends MsgHandler{
    
    public LeaveHandler(){
        super();
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        hopCount--;
        
        //Node to be leave from network.
        String leaverIp = st.nextToken();
        int leaverPort = Integer.parseInt(st.nextToken());
        NodeDTO leaver = new NodeDTO(leaverIp, leaverPort);
        
        //Update routes list and UI with new node
        routeInitializer.removeAndUpdate(leaver);
        routeInitializer.updateRoutesUI();
        
        //Send remove node message to all neighbours in routing table 
        Node[] neighbours = node.getRoutes();
        
        if (hopCount > 1) {
            for (Node neighbour : neighbours) {
                //Routes can have null values
                if (neighbour != null) {
                    msgSender.leave(neighbour, leaver, hopCount);
                }
            }
        }
        
        //Routes can have null values
        for(Node neighbour : neighbours){
            if (neighbour != null) {
                //pass update message to all neighbours in routing table 
                msgSender.updateRoutes(neighbour, node, null, hopCount);
            }
        }
    }
    
}
