package CS4262.Helpers.Messages;

import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class Active implements Message{
    
    private NodeDTO sender;
    private int hopCount;
    
    public String send(NodeDTO receiver, NodeDTO sender, int hopCount){
        this.sender = sender;
        this.hopCount = hopCount;
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    /*
    Update state message format 
    length ALIVE sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " ALIVE ";
        msg += hopCount + " " + sender.getIpAdress() + " " + sender.getPort();
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
                send(successor, sender, hopCount);
            }
        }
        
    }

}
