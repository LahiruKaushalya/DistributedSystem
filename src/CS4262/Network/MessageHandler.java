package CS4262.Network;

import CS4262.MainController;
import CS4262.Models.Node;

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
    
    public static MessageHandler getInstance() {
        if(instance == null){
            instance = new MessageHandler();
        }
        return instance;
    }
      
    private MessageHandler(){
        this.node = MainController.getInstance().getNode();
    }
    
    public void join(Node receiver){
        try {
            Thread t = new Thread() {
                String res;
                Socket socket;
                DataOutputStream outStream;
                DataInputStream inStream;
                
                @Override
                public void run() {
                    try {
                        socket = new Socket(receiver.getIpAdress(), receiver.getPort());
                        //Generate Join message
                        String message = generateJoinMsg(node);
                        // sends message to the socket
                        outStream = new DataOutputStream(socket.getOutputStream());
                        outStream.writeUTF(message);
                        
                        inStream = new DataInputStream(socket.getInputStream());
                        res = inStream.readUTF();
                        
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally{
                        try {
                            System.out.println(res);
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
        }
        catch (Exception ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String generateJoinMsg(Node sender){
        String msg = " JOIN ";
        msg += sender.getIpAdress() + " " + sender.getPort() + " " + sender.getId();
        return "00" + String.valueOf(msg.length() + 5) + msg;  
    }
}
