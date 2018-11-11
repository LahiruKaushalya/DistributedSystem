package CS4262.Core;


import CS4262.Interfaces.IInitializerFileIndex;
import CS4262.Interfaces.IInitializerWordIndex;
import CS4262.Models.DataTransfer.*;
import CS4262.Message.FileIndex.*;
import CS4262.Models.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Lahiru Kaushalya
 */
public class FileIndexInitializer implements IInitializerFileIndex, IInitializerWordIndex{
    
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
            NodeDTO receiver = findReceiver.search(file.getId());
            if(receiver != null){
                new AddSingleFileIndex().send(new MessageDTO(receiver, node, file));
            }
            else{
                List<NodeDTO> fileHolders = tempList.get(file.getId());
                if(fileHolders == null){
                    fileHolders = new ArrayList<>();
                    fileHolders.add(node);
                }
                else{
                    fileHolders.add(node);
                }
                tempList.put(file.getId(), fileHolders);
            }
            wordIndexInitializer.createLocalWordIndex(file);
        }
        node.setFileIndex(tempList);
        uiCreator.updateFileIndexUI();
    }
    
    //Create file index from predecessor's file insex (incoming msg)
    public void updateFromPre(NodeDTO sender, String fileID){
        Node successor = node.getSuccessor();
        if(successor != null){
            int sucID = idCreator.getComparableID(successor.getId());
            int nodeID = idCreator.getComparableID(node.getId());
            int fileIntID = idCreator.getComparableID(fileID);
            if(!rangeChecker.isInRange(nodeID, sucID, fileIntID)){
                localAdd(sender, fileID);
            }
        }
        else{
            localAdd(sender, fileID);
        }
    }
    
    //Modify file index when successor leaving (incoming msg)
    public void updateFromSuc(NodeDTO sender, String fileID){
        localAdd(sender, fileID);
    }
    
    //Activate file index backup when sucessor crash
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
        
        NodeDTO predecessor = node.getPredecessor();
        if(predecessor != null){
            new BackupFileIndex().send(new MessageDTO(node.getPredecessor()));
        }
    }
    
    //Notify nodes to remove file indices when leaveing (outgoing msg)
    public void sendRemoveMsg(){
        List<File> files = node.getContent();
        for(File file : files){
            NodeDTO receiver = findReceiver.search(file.getId());
            if(receiver != null){
                MessageDTO msg = new MessageDTO(receiver, node, file);
                new RemoveSingleFileIndex().send(msg);
            }
        }
    }
    
    //Send local file Index to predecessor when leaving(outgoing msg)
    public void sendFileIndexToPre(){
        NodeDTO predecessor = node.getPredecessor();
        if (predecessor != null) {
            Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
            Iterator<Map.Entry<String, List<NodeDTO>>> iterator = fileIndex.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, List<NodeDTO>> entry = iterator.next();
                String fileID = entry.getKey();
                List<NodeDTO> fileHolders = entry.getValue();
                
                if(fileHolders.size() == 1){
                    NodeDTO fileHolder = fileHolders.get(0);
                    String fileHolderID = idCreator.generateNodeID(fileHolder.getipAdress(), fileHolder.getUdpPort());
                    if(fileHolderID.equals(node.getId())){
                        iterator.remove();
                    }
                }
                else{
                    for(NodeDTO fileHolder : fileHolders){
                        String fileHolderID = idCreator.generateNodeID(fileHolder.getipAdress(), fileHolder.getUdpPort());
                        if(fileHolderID.equals(node.getId())){
                            fileHolders.remove(fileHolder);
                        }
                    }
                    fileIndex.put(fileID, fileHolders);
                }
            }
            node.setFileIndex(fileIndex);
            new SendFileIndexToPre().send(new MessageDTO(node.getPredecessor()));
        }
    }
    
    //Delete file index
    public void clearFileIndex(){
        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        //Reset file index
        fileIndex.clear();
        node.setFileIndex(fileIndex);
        uiCreator.updateFileIndexUI();
    }
    
    //Add new file to index (incoming msg)
    public void insert(NodeDTO sender, File file){
        NodeDTO receiver = findReceiver.search(file.getId());
        if (receiver != null) {
            new AddSingleFileIndex().send(new MessageDTO(receiver, sender, file));
        } 
        else {
            localAdd(sender, file.getId());
        }
    }
    
    //Remove file from index (incoming msg)
    public void remove(NodeDTO sender, File file){
        NodeDTO receiver = findReceiver.search(file.getId());
        if (receiver != null) {
            new RemoveSingleFileIndex().send(new MessageDTO(receiver, sender, file));
        } 
        else {
            Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
            
            if (fileIndex.containsKey(file.getId())) {
                List<NodeDTO> fileHolders = fileIndex.get(file.getId());
                if (fileHolders.size() == 1) {
                    fileIndex.remove(file.getId());
                } 
                else {
                    for (NodeDTO fileHolder : fileHolders) {
                        if (idCreator.generateNodeID(fileHolder.getipAdress(), fileHolder.getUdpPort())
                                .equals(idCreator.generateNodeID(sender.getipAdress(), sender.getUdpPort()))) {

                            fileHolders.remove(fileHolder);
                            break;
                        }
                    }
                    fileIndex.put(file.getId(), fileHolders);
                }
                node.setFileIndex(fileIndex);
            }
        }
    }
    
    //Update file index when successor changed
    public void updateWhenSucChanged(){
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
        
        NodeDTO predecessor = node.getPredecessor();
        if(predecessor != null){
            new BackupFileIndex().send(new MessageDTO(predecessor));
        }
    }
    
    private void localAdd(NodeDTO sender, String fileID) {
        String senderID = idCreator.generateNodeID(sender.getipAdress(), sender.getUdpPort());

        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        List<NodeDTO> temp = fileIndex.get(fileID);
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(sender);
            fileIndex.put(fileID, temp);
            node.setFileIndex(fileIndex);
            uiCreator.updateFileIndexUI();
        } 
        else {
            boolean exists = false;
            for (NodeDTO nodeT : temp) {
                String nodeTID = idCreator.generateNodeID(nodeT.getipAdress(), nodeT.getUdpPort());
                if (nodeTID.equals(senderID)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                temp.add(sender);
                fileIndex.put(fileID, temp);
                node.setFileIndex(fileIndex);
                uiCreator.updateFileIndexUI();
            }
        }
    }
    
}
