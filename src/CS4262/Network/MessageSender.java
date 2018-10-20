package CS4262.Network;

import CS4262.MainController;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MessageSender {
    
    private final Node node;
    private final long MSG_TIMEOUT;
    
    private static MessageSender instance;
    private String response;
    
    public static MessageSender getInstance() {
        if(instance == null){
            instance = new MessageSender();
        }
        return instance;
    }
      
    private MessageSender(){
        this.node = MainController.getInstance().getNode();
        this.MSG_TIMEOUT = 2000;
    }
    
    private String sendMsg(NodeDTO receiver, String message){
        try {
            response = null;
            Thread t = new Thread() {
                DatagramSocket socket;
                @Override
                public void run() {
                    try {
                        DatagramPacket dp;
                        byte[] buf = new byte[65536];
                        socket = new DatagramSocket();
                        InetAddress receiverIP = InetAddress.getByName(receiver.getIpAdress());
                        int receiverPort = receiver.getPort();
                        
                        //Create datagrame packet
                        dp = new DatagramPacket(message.getBytes(), message.length(), receiverIP, receiverPort);
                        socket.send(dp);

                        dp = new DatagramPacket(buf, 65536);
                        socket.receive(dp);

                        response = new String(dp.getData(), 0, dp.getLength());
                    }
                    catch (IOException ex) {
                        Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                        socket.close();
                    }
                }
            };
            t.start();
            t.join(MSG_TIMEOUT);
        }
        catch (Exception ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            if(response == null){
                System.out.println("Error while communicating...");
            }
            return response;
        }
    }
    
    public String join(NodeDTO receiver, NodeDTO joiner, int hopCount){
        String message = generateJoinMsg(joiner, hopCount);
        return sendMsg(receiver, message);
    }
    
    public String updateRoutes(NodeDTO receiver, NodeDTO sender, ArrayList<Node> addi, int hopCount){
        String message = generateUpdateRoutesMsg(sender, addi, hopCount);
        return sendMsg(receiver, message);
    }
    
    public String updateFileIndex(NodeDTO receiver, NodeDTO sender, int hopCount){
        String message = generateUpdateFileIndexMsg(sender, hopCount);
        return sendMsg(receiver, message);
    }
    
    public String passFileIndex(NodeDTO receiver, String message){
        return sendMsg(receiver, message);
    }
    
    public String leave(NodeDTO receiver, NodeDTO remove, int hopCount){
        String message = generateLeaveMsg(remove, hopCount);
        return sendMsg(receiver, message);
    }
    
    public String updateSuccessor(NodeDTO receiver){
        String message = generateUpdateSuccessorMsg();
        return sendMsg(receiver, message);
    }
    
    public String updateState(NodeDTO receiver, NodeDTO sender, int hopCount){
        String message = generateUpdateStateMsg(sender, hopCount);
        return sendMsg(receiver, message);
    }
    
    /*
    Join message format 
    length JOIN sender_ip sender_port
    */
    private String generateJoinMsg(NodeDTO joiner, int hopCount){
        String msg = " JOIN ";
        msg += hopCount + " " + joiner.getIpAdress() + " " + joiner.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg;   
    }
    
    /*
    Update Routes message format 
    length UPDATE_ROUTES hop_count sender_ip sender_port nodes_count node1_ip node1_port ....
    */
    private String generateUpdateRoutesMsg(NodeDTO sender, ArrayList<Node> addi, int hopCount){
        String msg = " UPDATE_ROUTES ";
        msg += hopCount + " " + sender.getIpAdress() + " " + sender.getPort();
        
        Node[] neighbours = node.getRoutes();
        int count = 0;
        String tempStr = "";
        
        for(Node neighbour : neighbours){
            if(neighbour != null){
                count++;
                tempStr += " " + neighbour.getIpAdress() + " " + neighbour.getPort();
            }
        }
        
        if (addi != null) {
            if (!addi.isEmpty()) {
                count += addi.size();
                for (Node neighbour : addi) {
                    tempStr += " " + neighbour.getIpAdress() + " " + neighbour.getPort();
                }
            }
        }
        
        msg += " " + count + tempStr;
        return String.format("%04d", msg.length() + 5) + " " + msg;   
    }
    
    /*
    Update File Index message format 
    length UPDATE_INDEX hop_count sender_ip sender_port file_cound file_id_1 file_id_2 ....
    */
    private String generateUpdateFileIndexMsg(NodeDTO sender, int hopCount){
        String msg = " UPDATE_INDEX ";
        msg += hopCount + " " + sender.getIpAdress() + " " + sender.getPort();
        
        List<File> files = node.getContent();
        msg += " " + files.size();
        
        for(File file : files){
            msg += " " + file.getId();
        }
        return String.format("%04d", msg.length() + 5) + " " + msg;  
    }
    
    /*
    Leave message format 
    length LEAVE hop_count leaver_ip leaver_port
    */
    private String generateLeaveMsg(NodeDTO leaver, int hopCount){
        String msg = " LEAVE ";
        msg += hopCount + " " + leaver.getIpAdress() + " " + leaver.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg;   
    }
    
    /*
    Update successor message format 
    length ISALIVE sender_ip sender_port
    */
    private String generateUpdateSuccessorMsg(){
        String msg = " ISALIVE ";
        msg += node.getIpAdress() + " " + node.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg;   
    }
    
    /*
    Update state message format 
    length ALIVE sender_ip sender_port
    */
    private String generateUpdateStateMsg(NodeDTO sender, int hopCount){
        String msg = " ALIVE ";
        msg += hopCount + " " + sender.getIpAdress() + " " + sender.getPort();
        return String.format("%04d", msg.length() + 5) + " " + msg;   
    }
    
}
