package CS4262.Core;

import static CS4262.Core.Initializer.node;
import CS4262.Helpers.Messages.Active;
import CS4262.Helpers.Messages.BackupFileIndex;
import CS4262.Helpers.Messages.JoinMsg;
import CS4262.Helpers.Messages.Leave;
import CS4262.Helpers.Messages.UpdateRoutes;
import CS4262.Helpers.Messages.UpdateSuccessor;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Lahiru Kaushalya
 */
//This will recieve 2 random nodes in the bootstrap server
public class NodeInitializer implements Initializer{
    
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
        this.fileInitializer = ContentInitializer.getInstance();
        this.routeInitializer = RouteInitializer.getInstance();
        NodeInitializer.hopCount = 3;
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
                response = new JoinMsg().send(neighbour, node, hopCount);
                routeInitializer.addAndUpdate(neighbour);
                /*
                Handle response here
                */
            }
            uiCreator.updateRoutesUI();
        }
        
        //Create node content
        fileInitializer.createNodeContent();
        
        //Flood routing table 
        Node[] neighbours = node.getRoutes();
        for(Node neighbour : neighbours){
            if(neighbour != null){
                response = new UpdateRoutes().send(neighbour, node, null, hopCount);
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
                Node successor = node.getSuccessor();
                if(successor != null){
                    //Check successor node is alive
                    String response = new UpdateSuccessor().send(successor);
                    if(response == null){
                        retryCount++;
                    }
                    else {
                        retryCount = 0;
                    }
                    
                    if(retryCount == 2){
                        //Remove dead node from routing table
                        routeInitializer.removeAndUpdate(successor);
                        uiCreator.updateRoutesUI();
                        
                        //Inform neighbours 
                        Node[] neighbours = node.getRoutes();
                        for (Node neighbour : neighbours) {
                            if (neighbour != null) {
                                new Leave().send(neighbour, successor, hopCount);
                                new UpdateRoutes().send(neighbour, node, null, hopCount);
                            }
                        }
                        
                        //Activate file index backup
                        Map<String, List<NodeDTO>> fileIndexBackup = node.getFileIndexBackup();
                        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
                        
                        for(String fileID : fileIndexBackup.keySet()){
                            fileIndex.put(fileID, fileIndexBackup.get(fileID));
                        }
                        fileIndexBackup.clear();
                        node.setFileIndexBackup(fileIndexBackup);
                        node.setFileIndex(fileIndex);
                        
                        //update UI
                        uiCreator.updateFileIndexUI();
                        
                        new BackupFileIndex().send(node.getPredecessor());
                        
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
                Node successor = node.getSuccessor();
                if(successor != null){
                    //Check successor node is alive
                    String response = new Active().send(successor, node, 20);
                }
                else{
                    retryCount = 0;
                }
            }
        };
        return task;
    }
    
}
