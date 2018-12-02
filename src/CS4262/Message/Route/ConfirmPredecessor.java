package CS4262.Message.Route;

import static CS4262.Interfaces.IInitializerFileIndex.fileIndexInitializer;
import static CS4262.Interfaces.IInitializerWordIndex.wordIndexInitializer;
import static CS4262.Interfaces.IMain.idCreator;
import static CS4262.Interfaces.IMain.mainController;
import static CS4262.Interfaces.IMain.node;
import CS4262.Interfaces.IMessage;
import static CS4262.Interfaces.IMessage.msgSender;
import CS4262.Message.FileIndex.BackupFileIndex;
import CS4262.Message.FileIndex.SendFileIndexToSuc;
import CS4262.Message.WordIndex.SendWordIndexToSuc;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.Node;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class ConfirmPredecessor implements IMessage{
    
    MessageDTO msgDTO;
    
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Confirm sender message format 
    length CON_PRE sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " CON_PRE ";
        msg += node.getipAdress() + " " + node.getUdpPort();
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //sender node 
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        String senderID = idCreator.generateNodeID(senderIP, senderPort);
        
        Node sender = new Node(senderIP, senderPort, senderID);
        Node successor = node.getSuccessor();
        
        if(successor.getId().equals(sender.getId())){
            
            new UpdatePredecessor().send(new MessageDTO(successor, true));
            
            fileIndexInitializer.updateWhenSucChanged();
            wordIndexInitializer.updateWhenSucChanged();
            mainController.getMainFrame().updateSuccessorDetails(successor);
            
            new SendFileIndexToSuc().send(new MessageDTO(successor));
            new SendWordIndexToSuc().send(new MessageDTO(successor));
        }
    }
    
}

