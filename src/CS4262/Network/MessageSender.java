package CS4262.Network;

import CS4262.Models.DataTransfer.NodeDTO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MessageSender {
    
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
        this.MSG_TIMEOUT = 2000;
    }
    
    public String sendMsg(NodeDTO receiver, String message){
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
                System.out.println("Error while sending message...");
            }
            return response;
        }
    }
    
}
