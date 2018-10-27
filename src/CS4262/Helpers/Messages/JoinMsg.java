package CS4262.Helpers.Messages;

import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class JoinMsg implements Message{
    
    private NodeDTO joinedNode;
    private int hopCount;
    
    public String send(NodeDTO receiver, NodeDTO joinedNode, int hopCount){
        this.joinedNode = joinedNode;
        this.hopCount = hopCount;
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    /*
    Join message format 
    length JOIN sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " JOIN ";
        msg += hopCount + " " + joinedNode.getIpAdress() + " " + joinedNode.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg; 
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
                    send(neighbour, newNode, hopCount);
                }
            }
        }
        
        //Routes can have null values
        for(Node neighbour : neighbours){
            if (neighbour != null) {
                //pass update message to all neighbours in routing table 
                new UpdateRoutes().send(neighbour, node, null, hopCount);
            }
        }
    }
    
}
