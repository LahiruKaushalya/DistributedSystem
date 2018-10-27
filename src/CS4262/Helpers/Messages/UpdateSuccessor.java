package CS4262.Helpers.Messages;

import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UpdateSuccessor implements Message{
    
    public String send(NodeDTO receiver){
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
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
