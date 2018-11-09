package CS4262.Message.Route;

import CS4262.Interfaces.IInitializerRoute;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UpdateRoutes implements IMessage, IInitializerRoute{
    
    MessageDTO msgDTO;
    
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Update Routes message format 
    length UPDATE_ROUTES hop_count sender_ip sender_port nodes_count node1_ip node1_port ....
    */
    @Override
    public String createMsg() {
        
        int hopCount = msgDTO.getHopCount();
        NodeDTO sender = msgDTO.getSender();
        List<NodeDTO> addi = msgDTO.getAdditional();
        
        String msg = " UPDATE_ROUTES ";
        msg += hopCount + " " + sender.getipAdress() + " " + sender.getUdpPort();
        
        Node[] neighbours = node.getRoutes();
        int count = 0;
        String tempStr = "";
        
        for(Node neighbour : neighbours){
            if(neighbour != null){
                count++;
                tempStr += " " + neighbour.getipAdress() + " " + neighbour.getUdpPort();
            }
        }
        
        if (addi != null) {
            if (!addi.isEmpty()) {
                count += addi.size();
                for (NodeDTO neighbour : addi) {
                    tempStr += " " + neighbour.getipAdress() + " " + neighbour.getUdpPort();
                }
            }
        }
        
        msg += " " + count + tempStr;
        return String.format("%04d", msg.length() + 5) + " " + msg;   
    
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
        ArrayList<NodeDTO> addi = new ArrayList<>();
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
            uiCreator.updateRoutesUI();
        }
        
        String id = node.getId();
        String senderID = idCreator.generateNodeID(senderIP, senderPort);
        
        //Check whether it is own msg or expired
        if(!id.equals(senderID) && hopCount > 0){
            Node[] neighbours = node.getRoutes();
            //Routes can have null values
            for(Node neighbour : neighbours){
                if (neighbour != null) {
                    //pass update message to all neighbours in routing table 
                    send(new MessageDTO(neighbour, sender, hopCount, addi));
                }
            }
        }
    }

}
