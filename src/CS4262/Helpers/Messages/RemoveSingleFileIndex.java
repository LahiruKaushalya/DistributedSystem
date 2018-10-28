package CS4262.Helpers.Messages;

import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RemoveSingleFileIndex implements Message{
    
    private NodeDTO sender;
    private String fileID;
    
    public String send(NodeDTO receiver, NodeDTO sender, String fileID){
        this.sender = sender;
        this.fileID = fileID;
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    /*
    Remove File Index message format 
    length REMOVE_FILE_INDEX sender_ip sender_port file_id
    */
    @Override
    public String createMsg() {
        String msg = " REMOVE_FILE_INDEX ";
        msg += sender.getIpAdress() + " " + sender.getPort() + " " + fileID;
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    public void handle(StringTokenizer st) {
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        fileIndexInitializer.remove(sender, st.nextToken());
        uiCreator.updateFileIndexUI();
    }

}