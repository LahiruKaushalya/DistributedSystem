package CS4262.Message.Search;

import CS4262.Interfaces.IInitializerSearch;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Interfaces.IMessage;
import CS4262.Models.File;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.DataTransfer.SearchDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class FileSearchRequest implements IMessage, IInitializerSearch{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Search message format 
    length SER sender_ip sender_port hop_count file_name
    */
    @Override
    public String createMsg() {
        NodeDTO sender = msgDTO.getSender();
        SearchDTO searchDTO = msgDTO.getSearchDTO();
        String msg = " SER ";
        msg += sender.getipAdress() + " " 
                + sender.getUdpPort() + " " 
                + searchDTO.getHopCount() + " " 
                + searchDTO.getFile().getName();
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        String fileName = st.nextToken();

        File file = new File(fileName, idCreator.generateFileID(fileName));
        searchInitializer.globalFileSearch(sender, new SearchDTO(file, hopCount));

    }

    
}
