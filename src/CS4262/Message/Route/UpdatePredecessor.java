package CS4262.Message.Route;

import CS4262.Message.FileIndex.BackupFileIndex;
import CS4262.Models.Node;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UpdatePredecessor implements IMessage{
    
    MessageDTO msgDTO;
    
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Update predecessor message format 
    length PRE sender_ip sender_port
    */
    @Override
    public String createMsg() {
        String msg = " PRE ";
        String confirmed = "FALSE";
        if(msgDTO.isConfirmed()){
            confirmed = "TRUE";
        }
        msg += node.getipAdress() + " " + node.getUdpPort() + " " + confirmed;
        return String.format("%04d", msg.length() + 5) + " " + msg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //predecessor node 
        String preIP = st.nextToken();
        int prePort = Integer.parseInt(st.nextToken());
        String preID = idCreator.generateNodeID(preIP, prePort);
        Node predecessor = new Node(preIP, prePort, preID);
        
        String confirmed = st.nextToken();
        
        if(confirmed.equals("TRUE")){
            node.setPredecessor(predecessor);
            mainController.getMainFrame().updatePredecessorDetails(predecessor);

            //Send file index backup to new predecessor
            new BackupFileIndex().send(new MessageDTO(predecessor));
        }
        else{
            new ConfirmPredecessor().send(new MessageDTO(predecessor));
        }
    }
    
}
