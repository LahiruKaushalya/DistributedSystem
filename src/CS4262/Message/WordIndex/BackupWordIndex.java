package CS4262.Message.WordIndex;

import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.MessageDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class BackupWordIndex implements IMessage{
    
    @Override
    public String send(MessageDTO msgDTO){
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Backup word Index message format 
    length BACKUP_WI word_cound file_count1 word_name file_id1 file_id2 ....
    */
    @Override
    public String createMsg() {
        String msg = " BACKUP_WI ";
        Map<String, List<String>> wordIndex = node.getWordIndex();
        msg += wordIndex.size();
        
        for(String wordName : wordIndex.keySet()){
            List<String> fileIds = wordIndex.get(wordName);
            msg += " " + fileIds.size() + " " + wordName;
            for(String fileId : fileIds){
                msg += " " + fileId;
            }
        }
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        int wordCount = Integer.parseInt(st.nextToken());
        Map<String, List<String>> wordIndexBackup = node.getWordIndex();
        
        while (wordCount > 0) {
            int fileCount = Integer.parseInt(st.nextToken());
            String wordName = st.nextToken();
            List<String> fileIds = new ArrayList<>();
            while (fileCount > 0) {
                fileIds.add(st.nextToken());
                fileCount--;
            }
            wordIndexBackup.put(wordName, fileIds);
            wordCount--;
        }
        node.setWordIndexBackup(wordIndexBackup);
    }
    
}
