package CS4262.Message.FileIndex;

import CS4262.Interfaces.IInitializerFileIndex;
import CS4262.Models.DataTransfer.NodeDTO;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SendFileIndexToSuc implements IMessage, IInitializerFileIndex{
    
    @Override
    public String send(MessageDTO msgDTO){
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Update File Index message format 
    length SEND_FI_SUC file_cound file_id_1 ip port file_id_2 ip port ....
    */
    @Override
    public String createMsg() {
        String msg = " SEND_FI_SUC ";
        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        msg += fileIndex.size();
        
        for(String fileID : fileIndex.keySet()){
            List<NodeDTO> tempNodes = fileIndex.get(fileID);
            msg += " " + tempNodes.size() + " " + fileID;
            for(NodeDTO tempNode : tempNodes){
                msg += " " + tempNode.getipAdress() + " " + tempNode.getUdpPort();
            }
        }
        return String.format("%04d", msg.length() + 5) + " " + msg;  
    }
    
    @Override
    public void handle(StringTokenizer st) {
        
        int fileCount = Integer.parseInt(st.nextToken());
        
        while (fileCount > 0) {
            int nodeCount = Integer.parseInt(st.nextToken());
            String fileID = st.nextToken();
            while (nodeCount > 0) {
                NodeDTO fileHolder = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
                fileIndexInitializer.updateFromPre(fileHolder, fileID);
                nodeCount--;
            }
            fileCount--;
        }
        fileIndexInitializer.updateWhenSucChanged();
        uiCreator.updateFileIndexUI();
    }
    
}
