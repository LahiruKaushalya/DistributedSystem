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
public class Join implements IMessage, IInitializerRoute{
    
    MessageDTO msgDTO;
    
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Join message format 
    length JOIN sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " JOIN ";
        msg += msgDTO.getHopCount() + " " + msgDTO.getSender().getipAdress() + " " + msgDTO.getSender().getUdpPort();
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
        uiCreator.updateRoutesUI();
        
        //Send new update routes message to all neighbours in routing table 
        Node[] neighbours = node.getRoutes();
        
        if(hopCount > 0){
            for (Node neighbour : neighbours) {
                //Routes can have null values
                if (neighbour != null) {
                    send(new MessageDTO(neighbour, newNode, hopCount));
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
