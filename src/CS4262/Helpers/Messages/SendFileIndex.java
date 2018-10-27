package CS4262.Helpers.Messages;

import CS4262.Models.NodeDTO;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SendFileIndex implements Message{

    public String send(NodeDTO receiver){
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    /*
    Update File Index message format 
    length SEND_FILE_INDEX file_cound file_id_1 ip port file_id_2 ip port ....
    */
    @Override
    public String createMsg() {
        String msg = " SEND_FILE_INDEX ";
        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        msg += fileIndex.size();
        
        for(String fileID : fileIndex.keySet()){
            List<NodeDTO> tempNodes = fileIndex.get(fileID);
            msg += " " + tempNodes.size() + " " + fileID;
            for(NodeDTO tempNode : tempNodes){
                msg += " " + tempNode.getIpAdress() + " " + tempNode.getPort();
            }
        }
        return String.format("%04d", msg.length() + 5) + " " + msg;  
    }
    
    @Override
    public void handle(StringTokenizer st) {
        int fileCount = Integer.parseInt(st.nextToken());
        
        while(fileCount > 0){
            int nodeCount = Integer.parseInt(st.nextToken());
            String fileID = st.nextToken();
            while(nodeCount > 0){
                NodeDTO fileHolder = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
                contentInitializer.createFileIndex(fileHolder, fileID);
                nodeCount--;
            }
            fileCount--;
        }
        contentInitializer.updateFileIndex();
        contentInitializer.updateFileIndexUI();
    }
    
}
