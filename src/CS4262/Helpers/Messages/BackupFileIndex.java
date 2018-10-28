package CS4262.Helpers.Messages;

import static CS4262.Helpers.Messages.Message.node;
import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class BackupFileIndex implements Message{
    
    public String send(NodeDTO receiver){
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    } 
    
    /*
    Update File Index message format 
    length BACKUP_FILE_INDEX file_cound file_id_1 ip port file_id_2 ip port ....
    */
    @Override
    public String createMsg() {
        String msg = " BACKUP_FILE_INDEX ";
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
        Map<String, List<NodeDTO>> fileIndexBackup = new HashMap();
        
        while (fileCount > 0) {
            int nodeCount = Integer.parseInt(st.nextToken());
            String fileID = st.nextToken();
            List<NodeDTO> fileHolders = new ArrayList<>();
            while (nodeCount > 0) {
                NodeDTO fileHolder = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
                fileHolders.add(fileHolder);
                nodeCount--;
            }
            fileIndexBackup.put(fileID, fileHolders);
            fileCount--;
        }
        node.setFileIndexBackup(fileIndexBackup);
    }
    
}
