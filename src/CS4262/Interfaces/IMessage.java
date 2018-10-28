package CS4262.Interfaces;

import CS4262.Models.MessageDTO;
import CS4262.Network.MessageSender;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IMessage extends IMain{
    
    public MessageSender msgSender = MessageSender.getInstance();
    
    public String send(MessageDTO msgDTO);
    
    public String createMsg();
    
    /**
     *
     * @param st
     */
    public void handle(StringTokenizer st);
    
}
