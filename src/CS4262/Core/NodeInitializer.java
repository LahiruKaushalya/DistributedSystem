package CS4262.Core;

import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Network.MessageSender;
import CS4262.Models.NodeDTO;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Lahiru Kaushalya
 */
//This will recieve 2 random nodes in the bootstrap server
public class NodeInitializer {
    
    private final Node node;
    private final MessageSender msgSender;
    private final MainController mainController;
    private final RouteInitializer routeInitializer;
    private final ContentInitializer fileInitializer;
    
    private Timer sendNodeStateTimer;
    private Timer checkSuccessorTimer;
    private TimerTask sendNodeStateTask;
    private TimerTask checkSuccessorTask;
    private static int hopCount;
    private int retryCount;
    
    private static NodeInitializer instance; 
    
    public static NodeInitializer getInstance() {
        if(instance == null){
            instance = new NodeInitializer();
        }
        return instance;
    }
    
    private NodeInitializer() {
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.msgSender = MessageSender.getInstance();
        this.fileInitializer = ContentInitializer.getInstance();
        this.routeInitializer = RouteInitializer.getInstance();
        NodeInitializer.hopCount = 4;
        this.retryCount = 0;
    }

    public static int getHopCount() {
        return hopCount;
    }

    /**
     *
     * @param newNodes
     */
    public void initializeNode(ArrayList<NodeDTO> newNodes) {
        
        //Join network throgh two random nodes
        String response;
        if (newNodes != null) {
            for(NodeDTO neighbour : newNodes){
                response = msgSender.join(neighbour, node, hopCount);
                routeInitializer.addAndUpdate(neighbour);
                /*
                Handle response here
                */
            }
            routeInitializer.updateRoutesUI();
        }
        
        //Create node content
        fileInitializer.createNodeContent();
        
        //Flood routing table 
        Node[] neighbours = node.getRoutes();
        for(Node neighbour : neighbours){
            if(neighbour != null){
                response = msgSender.updateRoutes(neighbour, node, null, hopCount);
                /*
                Handle response here
                */
            }
        }
        //Schedule successor status checker
        long delay = 10000, Period = 10000;
        checkSuccessorTask = checkSuccessorState();
        checkSuccessorTimer = new Timer();
        checkSuccessorTimer.scheduleAtFixedRate(checkSuccessorTask, delay, Period);
        
        delay = 30000; Period = 30000;
        sendNodeStateTask = sendNodeState();
        sendNodeStateTimer = new Timer();
        sendNodeStateTimer.scheduleAtFixedRate(sendNodeState(), delay, Period);
        
    }
    
    public void cancelAllScheduledTasks(){
        checkSuccessorTask.cancel();
        sendNodeStateTask.cancel();
        sendNodeStateTimer.cancel();
        checkSuccessorTimer.cancel();
    }
    
    private TimerTask checkSuccessorState() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MessageSender ms = MessageSender.getInstance();
                Node successor = node.getSuccessor();
                if(successor != null){
                    //Check successor node is alive
                    String response = ms.updateSuccessor(successor);
                    if(response == null){
                        retryCount++;
                    }
                    else {
                        retryCount = 0;
                    }
                    
                    if(retryCount == 2){
                        routeInitializer.removeAndUpdate(successor);
                        routeInitializer.updateRoutesUI();
                        
                        Node[] neighbours = node.getRoutes();
                        for (Node neighbour : neighbours) {
                            if (neighbour != null) {
                                msgSender.leave(neighbour, successor, hopCount);
                                msgSender.updateRoutes(neighbour, node, null, hopCount);
                            }
                        }
                        retryCount = 0;
                    }
                }
            }
        };
        return task;
    }
    
    private TimerTask sendNodeState() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MessageSender ms = MessageSender.getInstance();
                Node successor = node.getSuccessor();
                if(successor != null){
                    //Check successor node is alive
                    String response = ms.updateState(successor, node, 20);
                }
                else{
                    retryCount = 0;
                }
            }
        };
        return task;
    }
    
}
