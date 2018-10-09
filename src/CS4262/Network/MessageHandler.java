package CS4262.Network;

import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    
    public void leave(Node receiver){
        String message = generateLeaveMsg(node);
        sendMsg(receiver, message);
    }
    
    private String generateJoinMsg(Node sender){
        String msg = " JOIN ";
        msg += sender.getIpAdress() + " " + sender.getPort();
        return "00" + String.valueOf(msg.length() + 5) + msg;  
    }
    
    private String generateLeaveMsg(Node sender){
        String msg = " LEAVE ";
        msg += sender.getIpAdress() + " " + sender.getPort() + " " + sender.getId();
        return "00" + String.valueOf(msg.length() + 5) + msg;  
    }
}
