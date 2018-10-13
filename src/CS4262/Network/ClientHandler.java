package CS4262.Network;

import CS4262.Core.NodeInitializer;
import CS4262.Core.RouteInitializer;
import CS4262.Helpers.IDCreator;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class ClientHandler extends Thread{
    
    private final Node node;
    private final Socket socket; 
    private final IDCreator idCreator;
    private final DataInputStream inStream;
    private final DataOutputStream outStream;
    private final MessageHandler msgHandler;
    private final RouteInitializer routeInitializer; 
    
    public ClientHandler(Node node, Socket socket, DataInputStream inStream, DataOutputStream outStream){
        this.node = node;
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
        this.idCreator = new IDCreator();
        this.msgHandler = MessageHandler.getInstance();
        this.routeInitializer = new RouteInitializer();
    }
    
    @Override
    public void run(){
        try {
            String incomingMsg;
            incomingMsg = inStream.readUTF();
            //Process incoming message
            String response = createResponse(incomingMsg);
            outStream.writeUTF(response);

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                inStream.close();
                outStream.close();
                socket.close();
            } 
            catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String createResponse(String incomingMsg){
        String response = "";
        StringTokenizer st = new StringTokenizer(incomingMsg, " ");

        String length = st.nextToken();
        String command = st.nextToken();
        
        if(command.equals("JOIN")){
            try{
                joinMsgHandler(st);
            }
            catch(Exception e){
                response = "0016 JOINOK 9999";
            }
            response = "0013 JOINOK 0";
        }
        if(command.equals("UPDATE_ROUTES")){
            try{
                updateRoutesMsgHandler(st);
            }
            catch(Exception e){
                response = "0025 UPDATE_ROUTESOK 9999";
            }
            response = "0023 UPDATE_ROUTESOK 0";
        }
        if(command.equals("LEAVE")){
            try{
                leaveMsgHandler(st);
            }
            catch(Exception e){
                response = "0017 LEAVEOK 9999";
            }
            response = "0014 LEAVEOK 0";
        }
        if(command.equals("REMOVE")){
            try{
                removeMsgHandler(st);
            }
            catch(Exception e){
                response = "0018 REMOVEOK 9999";
            }
            response = "0015 REMOVEOK 0";
        }
        return response;
    }
    
    private void joinMsgHandler(StringTokenizer st) {
        //New Node about to join network
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        NodeDTO newNode = new NodeDTO(ip, port);
        
        //Update routes list and UI with new node
        routeInitializer.addAndUpdate(newNode);
        routeInitializer.updateRoutesUI();
        
        //Send new update routes message to all neighbours in routing table 
        Node[] neighbours = node.getRoutes();
        for(Node neighbour : neighbours){
            //Routes can have null values
            if(neighbour != null){
                msgHandler.updateRoutes(neighbour, node, null, NodeInitializer.getHopCount());
            }
        }
    }
    
    private void updateRoutesMsgHandler(StringTokenizer st) {
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        hopCount--;
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        //Number of routes
        int routes = Integer.parseInt(st.nextToken());
        
        NodeDTO temp;
        //Collect nodes that does not include/replaced by a new node in routing table
        ArrayList<Node> addi = new ArrayList<>();
        //Update routing table 
        if(routes > 1){
            for(int i = 0; i < routes; i++){
                temp = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
                Node tempN;
                if(i != 0){
                    tempN = routeInitializer.addAndUpdate(temp);
                    if(tempN != null){
                        addi.add(tempN);
                    }
                }
            }
            routeInitializer.updateRoutesUI();
        }
        
        String id = node.getId();
        String senderID = idCreator.generateNodeID(senderIP, senderPort);
        
        //Check whether it is own msg and not expire
        if(!id.equals(senderID) && hopCount != 0){
            Node[] neighbours = node.getRoutes();
            //Routes can have null values
            for(Node neighbour : neighbours){
                if (neighbour != null) {
                    //pass update message to all neighbours in routing table 
                    msgHandler.updateRoutes(neighbour, node, null, hopCount);
                }
            }
        }
    }
    
    private void leaveMsgHandler(StringTokenizer st) {
        //Node to be leave from network.
        String removeIp = st.nextToken();
        int removePort = Integer.parseInt(st.nextToken());
        NodeDTO removeNode = new NodeDTO(removeIp, removePort);
        
        routeInitializer.removeAndUpdate(removeNode);
        routeInitializer.updateRoutesUI();
        
        Node successor = node.getSuccessor();
        if (successor != null) {
            msgHandler.removeNode(successor, node, removeNode);
        }
    }
    
    private void removeMsgHandler(StringTokenizer st) {
        // Remove msg sender
        String senderIp = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        String senderID = idCreator.generateNodeID(senderIp, senderPort);
        NodeDTO sender = new NodeDTO(senderIp, senderPort);
        
        //Node to be remove.
        String removeIp = st.nextToken();
        int removePort = Integer.parseInt(st.nextToken());
        NodeDTO removeNode = new NodeDTO(removeIp, removePort);
        
        routeInitializer.removeAndUpdate(removeNode);
        routeInitializer.updateRoutesUI();
        
        //Check whether it is own msg
        if(!senderID.equals(node.getId())){
            Node successor = node.getSuccessor();
            if(successor != null){
                msgHandler.removeNode(successor, sender, removeNode);
            }
        }
    }
    
}
