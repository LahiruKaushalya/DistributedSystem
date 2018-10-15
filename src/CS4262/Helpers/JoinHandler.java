package CS4262.Helpers;

import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class JoinHandler extends MsgHandler{
    
    public JoinHandler(){
        super();
    }

    @Override
    public void handle(StringTokenizer st) {
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        hopCount--;
        
        //New Node about to join network
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        NodeDTO newNode = new NodeDTO(ip, port);
        
        //Update routes list and UI with new node
        routeInitializer.addAndUpdate(newNode);
        routeInitializer.updateRoutesUI();
        
        //Send new update routes message to all neighbours in routing table 
        Node[] neighbours = node.getRoutes();
        
        if(hopCount > 0){
            for (Node neighbour : neighbours) {
                //Routes can have null values
                if (neighbour != null) {
                    msgSender.join(neighbour, newNode, hopCount);
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
