package CS4262.Helpers;

import CS4262.Core.NodeInitializer;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class StateHandler extends MsgHandler{
    
    public StateHandler(){
        super();
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
        routeInitializer.updateRoutesUI();
        
        if(!senderID.equals(node.getId()) && hopCount > 0){
            Node successor = node.getSuccessor();
            if(successor != null){
                msgSender.updateState(successor, sender, hopCount);
            }
        }
        
    }
    
}
