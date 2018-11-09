package CS4262.Core;

import CS4262.Helpers.TaskCheckSucessor;
import CS4262.Helpers.TaskSendLivePulse;
import CS4262.Interfaces.IInitializerContent;
import CS4262.Interfaces.IInitializerRoute;
import CS4262.Message.Route.*;
import CS4262.Message.FileIndex.BackupFileIndex;
import CS4262.Message.WordIndex.BackupWordIndex;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.DataTransfer.NodeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Lahiru Kaushalya
 */

//This will recieve 2 random nodes in the bootstrap server

public class NodeInitializer implements IInitializerRoute, IInitializerContent{
    
    private final TaskCheckSucessor checkSuccessor;
    private final TaskSendLivePulse sendLivePulse;
    
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
        NodeInitializer.hopCount = 3;
        this.retryCount = 0;
        this.checkSuccessor = new TaskCheckSucessor();
        this.sendLivePulse = new TaskSendLivePulse();
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
        checkSuccessor.startTask();
        
        //Schedule live pulse sender
        sendLivePulse.startTask();
        
    }
    
    public void cancelAllScheduledTasks(){
        checkSuccessor.stopTask();
        sendLivePulse.stopTask();
    }
    
    
    public void activateFileIndexBackup(){
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
                        
                        new BackupFileIndex().send(new MessageDTO(node.getPredecessor()));
    }
    
    public void activateWordIndexBackup() {
        Map<String, List<File>> wordIndexBackup = node.getWordIndexBackup();
        Map<String, List<File>> wordIndex = node.getWordIndex();

        for (String wordName : wordIndexBackup.keySet()) {
            wordIndex.put(wordName, wordIndexBackup.get(wordName));
        }
        wordIndexBackup.clear();
        node.setWordIndexBackup(wordIndexBackup);
        node.setWordIndex(wordIndex);

        //update UI
        uiCreator.updateWordIndexUI();

        new BackupWordIndex().send(new MessageDTO(node.getPredecessor()));
    }
    
}
