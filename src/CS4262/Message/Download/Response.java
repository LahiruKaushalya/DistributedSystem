package CS4262.Message.Download;

import CS4262.Interfaces.IInitializerDownload;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class Response implements IMessage, IInitializerDownload{

    @Override
    public String send(MessageDTO msgDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String createMsg() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handle(StringTokenizer st) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
