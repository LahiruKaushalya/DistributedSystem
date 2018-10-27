package CS4262.Helpers.Messages;

import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class Leave implements Message{
    
    private NodeDTO leaveNode;
    private int hopCount;
    
    public String send(NodeDTO receiver, NodeDTO leaveNode, int hopCount){
        this.leaveNode = leaveNode;
        this.hopCount = hopCount;
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    /*
    Leave message format 
    length LEAVE hop_count leaver_ip leaver_port
    */
    @Override
    public String createMsg() {
        String msg = " LEAVE ";
        msg += hopCount + " " + leaveNode.getIpAdress() + " " + leaveNode.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg; 
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
        
        if (hopCount > 0) {
            for (Node neighbour : neighbours) {
                //Routes can have null values
                if (neighbour != null) {
                    send(neighbour, leaver, hopCount);
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
