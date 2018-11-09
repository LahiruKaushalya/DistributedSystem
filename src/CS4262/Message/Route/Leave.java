package CS4262.Message.Route;

import CS4262.Interfaces.IInitializerRoute;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class Leave implements IMessage, IInitializerRoute{
    
    MessageDTO msgDTO;
    
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Leave message format 
    length LEAVE hop_count leaver_ip leaver_port
    */
    @Override
    public String createMsg() {
        String msg = " LEAVE ";
        msg += msgDTO.getHopCount() + " " + msgDTO.getSender().getipAdress() + " " + msgDTO.getSender().getUdpPort();
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
        uiCreator.updateRoutesUI();
        
        //Send remove node message to all neighbours in routing table 
        Node[] neighbours = node.getRoutes();
        
        if (hopCount > 0) {
            for (Node neighbour : neighbours) {
                //Routes can have null values
                if (neighbour != null) {
                    send(new MessageDTO(neighbour, leaver, hopCount));
                }
            }
        }
        
        //Routes can have null values
        for(Node neighbour : neighbours){
            if (neighbour != null) {
                //pass update message to all neighbours in routing table 
                new UpdateRoutes().send(new MessageDTO(neighbour, node, hopCount, null));
            }
        }
    }

}
