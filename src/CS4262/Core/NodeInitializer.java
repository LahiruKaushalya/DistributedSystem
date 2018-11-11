package CS4262.Core;

import CS4262.Helpers.TaskCheckSucessor;
import CS4262.Helpers.TaskSendLivePulse;
import CS4262.Interfaces.IInitializerContent;
import CS4262.Interfaces.IInitializerRoute;
import CS4262.Message.Route.*;
import CS4262.Message.FileIndex.BackupFileIndex;
import CS4262.Message.WordIndex.BackupWordIndex;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.*;

import java.util.ArrayList;


/**
 *
 * @author Lahiru Kaushalya
 */

//This will recieve 2 random nodes in the bootstrap server

public class NodeInitializer implements IInitializerRoute,IInitializerContent{
    
    private TaskCheckSucessor checkSuccessor;
    private TaskSendLivePulse sendLivePulse;
    
    private static int hopCount;
    
    private static NodeInitializer instance; 
    
    public static NodeInitializer getInstance() {
        if(instance == null){
            instance = new NodeInitializer();
        }
        return instance;
    }
    
    private NodeInitializer() {
        NodeInitializer.hopCount = 3;
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
                response = new Join().send(new MessageDTO(neighbour, node, hopCount));
                routeInitializer.addAndUpdate(neighbour);
                /*
                Handle response here
                */
            }
            uiCreator.updateRoutesUI();
        }
        
        //Create node content
        contentInitializer.createNodeContent();
        
        //Flood routing table 
        Node[] neighbours = node.getRoutes();
        for(Node neighbour : neighbours){
            if(neighbour != null){
                response = new UpdateRoutes().send(new MessageDTO(neighbour, node, hopCount, null));
                /*
                Handle response here
                */
            }
        }
        
        //Schedule successor status checker
        this.checkSuccessor = new TaskCheckSucessor();
        checkSuccessor.startTask();
        
        //Schedule live pulse sender
        this.sendLivePulse = new TaskSendLivePulse();
        sendLivePulse.startTask();
        
        //Send backup to predecessor
        NodeDTO predecessor = node.getPredecessor();
        if(predecessor != null){
            new BackupFileIndex().send(new MessageDTO(predecessor));
            new BackupWordIndex().send(new MessageDTO(predecessor));
        }
    }
    
    public void cancelAllScheduledTasks(){
        checkSuccessor.stopTask();
        sendLivePulse.stopTask();
    }
    
}
