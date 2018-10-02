package CS4262;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class BSConnector {

    private final String ipAddress;
    private final String username;
    private static String response;
    private final int port;
    private long TIMEOUT;
    private final MainController mainController;
    
    private ArrayList<ArrayList<String>> nodes;

    public BSConnector(String ipAddress, String username, int port) {

        this.ipAddress = ipAddress;
        this.username = username;
        this.port = port;
        this.TIMEOUT = 5000;
        this.mainController = new MainController();

        mainController.setIpAddress(ipAddress);
        mainController.setUsername(username);
        mainController.setPort(port);
    }

    public void connect() {
        
        try {
            Thread t = new Thread() {
                public void run() {
                    try {
                        String message = genarateConnectMsg();
                        DatagramPacket dp;
                        byte[] buf = new byte[1024];
                        DatagramSocket ds = new DatagramSocket();
                        InetAddress ip = InetAddress.getByName("localhost");

                        dp = new DatagramPacket(message.getBytes(), message.length(), ip, 55555);
                        ds.send(dp);

                        dp = new DatagramPacket(buf, 1024);
                        ds.receive(dp);

                        response = new String(dp.getData(), 0, dp.getLength());
                        ds.close();
                        
                    } catch (SocketException ex) {
                        Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.start();
            t.join(TIMEOUT);

        } catch (InterruptedException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if(response == null){
                mainController.getMainFrame().displayError("Server Error"); 
            } else {
                String processedResponse = processResponce(response);
                mainController.getMainFrame().updateConnctionResponce(processedResponse);
                new NodeInitializer().initializeNode(nodes);
            }
        }
    }

    private String genarateConnectMsg() {
        String message;
        message = " REG " + ipAddress + " " + String.valueOf(port) + " " + username;
        message = "00" + String.valueOf(message.length() + 5) + message;
        return message;
    }

    private String processResponce(String response) {

        StringTokenizer st = new StringTokenizer(response, " ");
        String out = response + "\n\n";
        String length = st.nextToken();
        String regResponse = st.nextToken();
        int noNodes = Integer.parseInt(st.nextToken());

        if (regResponse.equals("REGOK")) {
            switch (noNodes) {
                case 0:
                    out += "Registration success.\nNo other nodes available";
                    return out;
                case 9999:
                    out += "Registration failed.\nThere is some error in the command";
                    return out;
                case 9998:
                    out += "Registration failed.\nAlready registered to you.\nUnregister first";
                    return out;
                case 9997:
                    out += "Registration failed.\nRegistered to another user.\nTry a different IP and port";
                    return out;
                case 9996:
                    out += "Registration failed.\nCan not register. BS full.";
                    return out;
                default:
                    out += "Registration success. " + noNodes + " nodes available\n\nIP Address\tPort\n";
                    this.nodes = new ArrayList<ArrayList<String>>();
                    ArrayList<String> temp; 
                    for (int i = 0; i < noNodes; i++) {
                        String ip = st.nextToken();
                        String port = st.nextToken();
                        temp = new ArrayList<String>();
                        temp.add(ip);
                        temp.add(port);
                        nodes.add(temp);
                        out += ip + "\t" + port + "\n";
                    }
                    return out;
            }
        } else {
            out += "Registration failed.";
            return out;
        }
    }
}
