package CS4262.Message.FileIndex;

import CS4262.Interfaces.IInitializerFileIndex;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.File;
import CS4262.Models.MessageDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RemoveSingleFileIndex implements IMessage, IInitializerFileIndex{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Remove File Index message format 
    length REMOVE_FI sender_ip sender_port file_name file_id
    */
    @Override
    public String createMsg() {
        NodeDTO sender = msgDTO.getSender();
        File file = msgDTO.getFile();
        
        String msg = " REMOVE_FI ";
        msg += sender.getIpAdress() + " " + sender.getPort() + " " + file.getName() + " " + file.getId();
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    public void handle(StringTokenizer st) {
        //Message sender
        NodeDTO sender = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
        //File
        File file = new File(st.nextToken(), st.nextToken());
        
        fileIndexInitializer.remove(sender, file);
        uiCreator.updateFileIndexUI();
    }

}
