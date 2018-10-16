package CS4262.Network;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import CS4262.Core.NodeInitializer;

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
    
    private static Node node;
    private static String response;
    
    private final long CON_TIMEOUT;
    private final String bsIPAddress;
    private final MainController mainController;
    private final MessageSender msgSender;
    private final IDCreator idCreator;
    
    private ArrayList<NodeDTO> nodes;
    private NodeServer nodeServerThread;;

    public BSConnector(String bsipAddress, String ipAddress, int port) {
        
        this.CON_TIMEOUT = 5000;
        this.bsIPAddress = bsipAddress;
        this.mainController = MainController.getInstance();
        this.idCreator = new IDCreator();
        
        //node initialization
        String nodeID = idCreator.generateNodeID(ipAddress, port);
        BSConnector.node = new Node(ipAddress, port, nodeID);
        mainController.setNode(node);
        mainController.getMainFrame().updateNodeDetails(node);
        
        //After node initialization
        this.msgSender = MessageSender.getInstance();
    }
    
    public void register() {
        
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    send(" REG ");
                }
            };
            t.start();
            t.join(CON_TIMEOUT);

        } 
        catch (InterruptedException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally{
            if(response == null){
                mainController.getMainFrame().displayError("Server Error"); 
            } else {
                //Process bootstrap server responce
                String processedResponse = processResponce(response);
                //Update UI
                mainController.getMainFrame().updateConnctionResponce(processedResponse);
            }
        }
    }
    
    public void unregister() {
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    send(" UNREG ");
                }
            };
            t.start();
            t.join(CON_TIMEOUT);
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally{
            if(response == null){
                mainController.getMainFrame().displayError("Server Error"); 
            } else {
                String processedResponse = processResponce(response);
                mainController.getMainFrame().updateConnctionResponce(processedResponse);
            }
        }
    }
    
    private void send(String code){
        try {
            String message = genarateMsg(code);
            DatagramPacket dp;
            byte[] buf = new byte[1024];
            DatagramSocket ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(bsIPAddress);
            
            dp = new DatagramPacket(message.getBytes(), message.length(), ip, 55555);
            ds.send(dp);
            
            dp = new DatagramPacket(buf, 1024);
            ds.receive(dp);
            
            response = new String(dp.getData(), 0, dp.getLength());
            ds.close();
        } 
        catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String genarateMsg(String code) {
        String message;
        String ip = node.getIpAdress();
        int port = node.getPort();
        message = code + ip + " " + port + " " + idCreator.generateNodeID(ip, port);
        message = "00" + String.valueOf(message.length() + 5) + message;
        return message;
    }

    private String processResponce(String response) {
        StringTokenizer st = new StringTokenizer(response, " ");
        String out = response + "\n\n";
        String length = st.nextToken();
        String resCode = st.nextToken();
        int value = Integer.parseInt(st.nextToken());

        switch (resCode) {
            case "REGOK":
                switch (value) {
                    case 0:
                        out += "Registration successful.\nNo other nodes available";
                        onRegSuccess();
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
                        out += "Registration success.\n\nIP Address\tPort\n";
                        this.nodes = new ArrayList<>();
                        for (int i = 0; i < value; i++) {
                            String ip = st.nextToken();
                            String port = st.nextToken();
                            nodes.add(new NodeDTO(ip, Integer.parseInt(port)));
                            out += ip + "\t" + port + "\n";
                        }
                        onRegSuccess();
                        return out;
                }
            case "UNROK":
                switch(value){
                    case 0:
                        out += "Unregistration successful.";
                        onUnregSuccess();
                        return out;
                    default:
                        out += "Error while unregistering.\nIP and port may not be in the registry or command is incorrect.";
                        return out;
                }
            default:
                out += "Error";
                return out;
        }
    }
    
    private void onRegSuccess(){
        //Start Node TCP Server
        nodeServerThread = NodeServer.getInstance(node);
        nodeServerThread.startServer();
        //Initialize neighbours
        NodeInitializer.getInstance().initializeNode(nodes);
    }
    
    private void onUnregSuccess(){
        Node[] neighbours = node.getRoutes();
        try {  
            Thread t = new Thread(){
                @Override
                public void run(){
                    int count = 0;
                    for (Node neighbour : neighbours) {
                        if(neighbour != null){
                            count++;
                            msgSender.leave(neighbour, node, NodeInitializer.getHopCount());
                            if(count == 1){
                                msgSender.updateRoutes(neighbour, node, null, NodeInitializer.getHopCount());
                            }
                        }
                    }
                }
            };
            t.start();
            t.join(CON_TIMEOUT);
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(BSConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            nodeServerThread.stopServer();
            NodeInitializer.getInstance().cancelAllScheduledTasks();
        }
    }
}
