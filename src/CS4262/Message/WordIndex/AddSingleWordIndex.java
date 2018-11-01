package CS4262.Message.WordIndex;

import CS4262.Interfaces.IInitializerWordIndex;
import CS4262.Interfaces.IMessage;
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
    Add File Index message format 
    length ADD_WORD_INDEX word_name word_id file_id
    */
    @Override
    public String createMsg() {
        Word word = msgDTO.getWord();
        String fileID = msgDTO.getFileNameOrID();
        
        String msg = " ADD_WORD_INDEX ";
        msg += word.getName() + " " + word.getId() + " " + fileID;
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    public void handle(StringTokenizer st) {
        String name = st.nextToken();
        String id = st.nextToken();
        String fileID = st.nextToken();
        wordIndexInitializer.insert(new Word(name, id), fileID);
        uiCreator.updateFileIndexUI();
    }

}
