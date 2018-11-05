package CS4262.Message.WordIndex;

import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.File;
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
    length BACKUP_WI word_cound file_count1 word_name file_name1 file_id1 ....
    */
    @Override
    public String createMsg() {
        String msg = " BACKUP_WI ";
        Map<String, List<File>> wordIndex = node.getWordIndex();
        msg += wordIndex.size();
        
        for(String wordName : wordIndex.keySet()){
            List<File> files = wordIndex.get(wordName);
            msg += " " + files.size() + " " + wordName;
            for(File file : files){
                msg += " " + file.getName() + " " + file.getId();
            }
        }
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        int wordCount = Integer.parseInt(st.nextToken());
        Map<String, List<File>> wordIndexBackup = node.getWordIndex();
        
        while (wordCount > 0) {
            int fileCount = Integer.parseInt(st.nextToken());
            String wordName = st.nextToken();
            List<File> files = new ArrayList<>();
            while (fileCount > 0) {
                files.add(new File(st.nextToken(), st.nextToken()));
                fileCount--;
            }
            wordIndexBackup.put(wordName, files);
            wordCount--;
        }
        node.setWordIndexBackup(wordIndexBackup);
    }
    
}
