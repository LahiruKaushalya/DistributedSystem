package CS4262.Message.WordIndex;

import CS4262.Interfaces.IInitializerWordIndex;
import CS4262.Interfaces.IMessage;
import CS4262.Models.File;
import CS4262.Models.MessageDTO;
import CS4262.Models.NodeDTO;
import CS4262.Models.Word;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class AddSingleWordIndex implements IMessage, IInitializerWordIndex{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Add Word Index message format 
    length ADD_WI word_name word_id file_name file_id
    */
    @Override
    public String createMsg() {
        Word word = msgDTO.getWord();
        File file = msgDTO.getFile();
        
        String msg = " ADD_WI ";
        msg += word.getName() + " " + word.getId() + " " + file.getName() + " " + file.getId();
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    public void handle(StringTokenizer st) {
        Word word = new Word(st.nextToken(), st.nextToken());
        File file = new File(st.nextToken(), st.nextToken());
        wordIndexInitializer.insert(word, file);
        uiCreator.updateFileIndexUI();
    }

}
