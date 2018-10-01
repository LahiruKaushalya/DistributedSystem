package CS4262;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class BSConnector {
    
    private String ipAddress;
    private String username;
    private int port;
    
    public BSConnector(String ipAddress, String username, int port){
        this.ipAddress = ipAddress;
        this.username = username;
        this.port = port;
    }
    
    public void connect(){
        try {
            String message = genarateConnectMsg();
            
            DatagramPacket dp;
            byte[] buf = new byte[1024];
            DatagramSocket ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(ipAddress);

            dp = new DatagramPacket(message.getBytes(), message.length(), ip, 55555);
            ds.send(dp);
            
            dp = new DatagramPacket(buf, 1024);
            ds.receive(dp);
            
            String responce = new String(dp.getData(), 0, dp.getLength());
            
            new MainController().getMainFrame().updateConnctionResponce(responce);
            ds.close(); 
            
        } catch (SocketException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String genarateConnectMsg(){
        String message;
        message = " REG " + ipAddress + " " + String.valueOf(port) + " " + username;
        message = "00" + String.valueOf(message.length() + 5) + message;
        return message;
    }
}
