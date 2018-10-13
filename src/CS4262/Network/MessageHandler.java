package CS4262.Network;

import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MessageHandler {
    
    private final Node node;
    
    private static MessageHandler instance;
    private String response;
    
    public static MessageHandler getInstance() {
        if(instance == null){
            instance = new MessageHandler();
        }
        return instance;
    }
      
    private MessageHandler(){
        this.node = MainController.getInstance().getNode();
    }
    
    public String sendMsg(NodeDTO receiver, String msg){
        try {
            Thread t = new Thread() {
                Socket socket;
                DataOutputStream outStream;
                DataInputStream inStream;
                
                @Override
                public void run() {
                    try {
                        socket = new Socket(receiver.getIpAdress(), receiver.getPort());
                        
                        // sends message to the socket
                        outStream = new DataOutputStream(socket.getOutputStream());
                        outStream.writeUTF(msg);
                        
                        inStream = new DataInputStream(socket.getInputStream());
                        response = inStream.readUTF();
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally{
                        try {
                            inStream.close();
                            outStream.close();
                            socket.close();
                        } 
                        catch (IOException ex) {
                            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            t.start();
            t.join(2000);
        }
        catch (Exception ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    public String join(NodeDTO receiver){
        String message = generateJoinMsg(node);
        return sendMsg(receiver, message);
    }
    
    public String updateRoutes(NodeDTO receiver, NodeDTO sender, ArrayList<Node> addi, int hopCount){
        String message = generateUpdateRoutesMsg(sender, addi, hopCount);
        return sendMsg(receiver, message);
    }
    
    public String removeNode(NodeDTO receiver, NodeDTO sender, NodeDTO remove){
        String message = generateRemoveNodeMsg(sender, remove);
        return sendMsg(receiver, message);
    }
    
    public String leave(NodeDTO receiver){
        String message = generateLeaveMsg(node);
        return sendMsg(receiver, message);
    }
    
    /*
    Join message format 
    length JOIN sender_ip sender_port
    */
    private String generateJoinMsg(NodeDTO sender){
        String msg = " JOIN ";
        msg += sender.getIpAdress() + " " + sender.getPort();
        return "00" + String.valueOf(msg.length() + 5) + msg;  
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
            if (addi.size() != 0) {
                count += addi.size();
                for (Node neighbour : addi) {
                    tempStr += " " + neighbour.getIpAdress() + " " + neighbour.getPort();
                }
            }
        }
        
        msg += " " + count + tempStr;
        return "00" + String.valueOf(msg.length() + 5) + msg;  
    }
    
    /*
    Remove message format 
    length REMOVE sender_ip sender_port remove_node_ip remove_node_port
    */
    private String generateRemoveNodeMsg(NodeDTO sender, NodeDTO remove){
        String msg = " REMOVE ";
        msg += sender.getIpAdress() + " " + sender.getPort();
        msg += " " + remove.getIpAdress() + " " + remove.getPort();
        return "00" + String.valueOf(msg.length() + 5) + msg;  
    }
    
    /*
    Leave message format 
    length LEAVE sender_ip sender_port
    */
    private String generateLeaveMsg(NodeDTO sender){
        String msg = " LEAVE ";
        msg += sender.getIpAdress() + " " + sender.getPort();
        return "00" + String.valueOf(msg.length() + 5) + msg;  
    }
}
