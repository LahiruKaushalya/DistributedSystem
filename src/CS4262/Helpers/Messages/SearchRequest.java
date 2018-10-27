package CS4262.Helpers.Messages;

import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchRequest implements Message{
    
    private NodeDTO sender;
    private String fileName;
    
    public String send(NodeDTO receiver, NodeDTO sender, String fileName){
        this.sender = sender;
        this.fileName = fileName;
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    /*
    Search message format 
    length SER sender_ip sender_port file_name
    */
    @Override
    public String createMsg() {
        String msg = " SER ";
        msg += sender.getIpAdress() + " " + sender.getPort() + " " + fileName;
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        String searchFile = "";
        while(st.hasMoreTokens()){
            searchFile += st.nextToken() + " ";
        }
        
        String SenderID = idCreator.generateNodeID(senderIP, senderPort);
        if(!SenderID.equals(node.getId())){
            searchInitializer.globalSearch(sender, searchFile.trim());
        }
    }

    
}
