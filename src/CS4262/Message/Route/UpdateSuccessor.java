package CS4262.Message.Route;

import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.MessageDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UpdateSuccessor implements IMessage{
    
    @Override
    public String send(MessageDTO msgDTO){
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Update successor message format 
    length ISALIVE sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " ISALIVE ";
        msg += node.getIpAdress() + " " + node.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        
    }

}
