package CS4262.Message.WordIndex;

import CS4262.Interfaces.IInitializerWordIndex;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.File;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.Word;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SendWordIndexToSuc implements IMessage, IInitializerWordIndex{
    
    @Override
    public String send(MessageDTO msgDTO){
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Update File Index message format 
    length SEND_WI_SUC word_cound file_count word_name file_id_1 file_id_2... file_count word_name file_id_1 file_id_2....
    */
    @Override
    public String createMsg() {
        String msg = " SEND_WI_SUC ";
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
        
        while (wordCount > 0) {
            int fileCount = Integer.parseInt(st.nextToken());
            String wordName = st.nextToken();
            while (fileCount > 0) {
                File file = new File(st.nextToken(), st.nextToken());
                Word word = new Word(wordName, idCreator.generateWordID(wordName));
                wordIndexInitializer.updateFromPre(word, file);
                fileCount--;
            }
            wordCount--;
        }

        wordIndexInitializer.updateForSuccessor();
        uiCreator.updateWordIndexUI();
    }
    
}
