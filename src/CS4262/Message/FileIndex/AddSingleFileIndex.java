package CS4262.Message.FileIndex;

import CS4262.Interfaces.IInitializerFileIndex;
import CS4262.Interfaces.IMessage;
import CS4262.Models.MessageDTO;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class AddSingleFileIndex implements IMessage, IInitializerFileIndex{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Add File Index message format 
    length ADD_FI sender_ip sender_port file_id
    */
    @Override
    public String createMsg() {
        NodeDTO sender = msgDTO.getSender();
        String fileID = msgDTO.getFileNameOrID();
        
        String msg = " ADD_FI ";
        msg += sender.getIpAdress() + " " + sender.getPort() + " " + fileID;
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    public void handle(StringTokenizer st) {
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        fileIndexInitializer.insert(sender, st.nextToken());
        uiCreator.updateFileIndexUI();
    }

}
