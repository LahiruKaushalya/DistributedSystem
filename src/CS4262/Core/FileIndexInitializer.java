package CS4262.Core;

import static CS4262.Core.Initializer.idCreator;
import static CS4262.Core.Initializer.mainController;
import static CS4262.Core.Initializer.node;
import static CS4262.Core.Initializer.rangeChecker;
import CS4262.Helpers.Messages.AddSingleFileIndex;
import CS4262.Helpers.Messages.RemoveSingleFileIndex;
import CS4262.Helpers.Messages.SendFileIndex;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lahiru Kaushalya
 */
public class FileIndexInitializer implements Initializer{
    
    private static FileIndexInitializer instance;
    
    public static FileIndexInitializer getInstance(){
        if(instance == null){
            instance = new FileIndexInitializer();
            return instance;
        }
        return instance;
    }
    
    private FileIndexInitializer(){}
    
    public void createLocalFileIndex(){
        List<File> content = node.getContent();
        Map<String, List<NodeDTO>> tempList = node.getFileIndex();
        for(File file : content){
            NodeDTO receiver = getReceiver(file.getId());
            if(receiver != null){
                new AddSingleFileIndex().send(receiver, node, file.getId());
            }
            else{
                List<NodeDTO> t = new ArrayList<NodeDTO>();
                t.add(node);
                tempList.put(file.getId(), t);
            }
        }
        node.setFileIndex(tempList);
        uiCreator.updateFileIndexUI();
    }
    
    //Find relevent node to index content files
    public NodeDTO getReceiver(String fileID){
        NodeDTO receiver = null;
        int fileIntID = idCreator.getComparableID(fileID);
        int nodeIntID = idCreator.getComparableID(node.getId());
        Node[] neighbours = node.getRoutes();
        
        for(Node neighbour : neighbours){
            if(neighbour != null){
                int neiIntID = idCreator.getComparableID(neighbour.getId());
                if(rangeChecker.isInRange(nodeIntID, neiIntID, fileIntID)){
                    receiver = neighbour;
                }
                else{break;}
            }
        }
        return receiver;
    }
    
    //Notify nodes to remove file indices when leave (outgoing msg)
    public void sendRemoveMsg(){
        List<File> files = node.getContent();
        for(File file : files){
            NodeDTO receiver = getReceiver(file.getId());
            if(receiver != null){
                new RemoveSingleFileIndex().send(receiver, node, file.getId());
            }
        }
    }
    
    //Send local file Index to predecessor and remove (outgoing msg)
    public void removeFileIndex(){
        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        
        for(String fileID : fileIndex.keySet()){
            List<NodeDTO> fileHolders = fileIndex.get(fileID);
            if(fileHolders.size() == 1){
                String fileHolderID = idCreator.generateNodeID(fileHolders.get(0).getIpAdress(), fileHolders.get(0).getPort());
                if(fileHolderID.equals(node.getId())){
                    fileIndex.remove(fileID);
                }
            }
            else{
                for(NodeDTO fileHolder : fileHolders){
                    String fileHolderID = idCreator.generateNodeID(fileHolder.getIpAdress(), fileHolder.getPort());
                    if(fileHolderID.equals(node.getId())){
                        fileHolders.remove(fileHolder);
                    }
                }
                fileIndex.put(fileID, fileHolders);
            }
        }
        node.setFileIndex(fileIndex);
        new SendFileIndex().send(node.getPredecessor(), node);
        
        //Reset file index
        fileIndex.clear();
        node.setFileIndex(fileIndex);
        uiCreator.updateFileIndexUI();
    }
    
    //Add new file to index (incoming msg)
    public void insert(NodeDTO sender, String fileID){
        NodeDTO receiver = getReceiver(fileID);
        if (receiver != null) {
            new AddSingleFileIndex().send(receiver, sender, fileID);
        } 
        else {
            localAdd(sender, fileID);
        }
    }
    
    //Remove file index (incoming msg)
    public void remove(NodeDTO sender, String fileID){
        NodeDTO receiver = getReceiver(fileID);
        if (receiver != null) {
            new RemoveSingleFileIndex().send(receiver, sender, fileID);
        } 
        else {
            Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();

            if (fileIndex.containsKey(fileID)) {
                List<NodeDTO> fileHolders = fileIndex.get(fileID);
                if (fileHolders.size() == 1) {
                    fileIndex.remove(fileID);
                } 
                else {
                    for (NodeDTO fileHolder : fileHolders) {
                        if (idCreator.generateNodeID(fileHolder.getIpAdress(), fileHolder.getPort())
                                .equals(idCreator.generateNodeID(sender.getIpAdress(), sender.getPort()))) {

                            fileHolders.remove(fileHolder);
                            break;
                        }
                    }
                    fileIndex.put(fileID, fileHolders);
                }
                node.setFileIndex(fileIndex);
            }
        }
    }
    
    //Create file index from predecessor's file insex (incoming msg)
    public void insertFromPredecessor(NodeDTO sender, String fileID){
        localAdd(sender, fileID);
    }
    
    //Update when successor changed
    public void updateForSuccessor(){
        Node successor = node.getSuccessor();
        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        
        int nodeID = idCreator.getComparableID(node.getId());
        int succID = idCreator.getComparableID(successor.getId());
        int fileIntID;
        
        Iterator<Map.Entry<String, List<NodeDTO>>> iterator = fileIndex.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, List<NodeDTO>> entry = iterator.next();
            String fileID = entry.getKey();
            fileIntID = idCreator.getComparableID(fileID);
            
            if(rangeChecker.isInRange(nodeID, succID, fileIntID)){
                iterator.remove();
            }
        }
        node.setFileIndex(fileIndex);
        uiCreator.updateFileIndexUI();
    }
    
    private void localAdd(NodeDTO sender, String fileID) {
        String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());

        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        List<NodeDTO> temp = fileIndex.get(fileID);
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(sender);
            fileIndex.put(fileID, temp);
            node.setFileIndex(fileIndex);
        } 
        else {
            boolean exists = false;
            for (NodeDTO nodeT : temp) {
                String nodeTID = idCreator.generateNodeID(nodeT.getIpAdress(), nodeT.getPort());
                if (nodeTID.equals(senderID)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                temp.add(sender);
                fileIndex.put(fileID, temp);
                node.setFileIndex(fileIndex);
            }
        }
    }
    
}
