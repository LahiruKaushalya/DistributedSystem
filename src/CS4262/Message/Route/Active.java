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
public class Active implements IMessage, IInitializerRoute{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Update state message format 
    length ALIVE sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " ALIVE ";
        msg += msgDTO.getHopCount() + " " + msgDTO.getSender().getIpAdress() + " " + msgDTO.getSender().getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        hopCount--;
        
        //existing node sending state
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        String senderID = idCreator.generateNodeID(senderIP, senderPort);
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        //Update routes list and UI with new node
        routeInitializer.addAndUpdate(sender);
        uiCreator.updateRoutesUI();
        
        if(!senderID.equals(node.getId()) && hopCount > 0){
            Node successor = node.getSuccessor();
            if(successor != null){
                send(new MessageDTO(successor, sender, hopCount));
            }
        }
        
    }

}
