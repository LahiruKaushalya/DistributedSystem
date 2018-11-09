package CS4262.Message.Download;

import CS4262.Interfaces.IInitializerDownload;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Models.File;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class DownloadRequest implements IMessage, IInitializerDownload{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO) {
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    File download message format 
    length DOWN sender_ip sender_port file_id
    */
    @Override
    public String createMsg() {
        NodeDTO sender = msgDTO.getSender();
        File file = msgDTO.getFile();
        
        String msg = " DOWN ";
        msg += sender.getipAdress() + " " + sender.getUdpPort() + " " + file.getName() + " " + file.getId();
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //Download requester
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        NodeDTO requester = new NodeDTO(ip, port);
        
        //File
        String fileName = st.nextToken();
        String fileID = st.nextToken();
        File file = new File(fileName, fileID);
        
        downloadInitializer.uploadFile(requester, file);
        
    }
    
}
