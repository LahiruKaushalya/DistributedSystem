package CS4262.Core;

import CS4262.Helpers.Messages.SingleFileIndex;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Lahiru Kaushalya
 */
public class ContentInitializer implements Initializer {
    
    private final String[] availableFiles;
    
    private static ContentInitializer instance;
    
    public static ContentInitializer getInstance(){
        if(instance == null){
            instance = new ContentInitializer();
        }
        return instance;
    }
    
    private ContentInitializer(){
        this.availableFiles = new String[]{
            "Adventures of Tintin",
            "Jack and Jill",
            "Glee",
            "The Vampire Diarie",
            "King Arthur",
            "Windows XP",
            "Harry Potter",
            "Kung Fu Panda",
            "Lady Gaga",
            "Twilight",
            "Windows 8",
            "Mission Impossible",
            "Turn Up The Music",
            "Super Mario",
            "American Pickers",
            "Microsoft Office 2010",
            "Happy Feet",
            "Modern Family",
            "American Idol",
            "Hacking for Dummies"
        };
    }
    
    public void createNodeContent(){
        
        Random ran = new Random();
        int numOfFiles = 3 + ran.nextInt(3);
        
        List<File> content = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        
        String text = "File ID\tFile Name\n\n",fileName;
        
        //genatate random number of files from available files
        int x = 0, index;
        while(x < numOfFiles){
            index = ran.nextInt(availableFiles.length - 1);
            //Avoid file duplications
            if(!temp.contains(index)){
                fileName = availableFiles[index];
                String fileID = idCreator.generateFileID(fileName);
                content.add(new File(fileName, fileID));
                text += fileID + "\t" + fileName + "\n";
                temp.add(index);
                x++;
            }
        }
        //set node content
        node.setContent(content);
        //update UI
        mainController.getMainFrame().updateContent(text);
        
        Map<String, List<NodeDTO>> tempList = node.getFileIndex();
        for(File file : content){
            NodeDTO receiver = getReceiver(file.getId());
            if(receiver != null){
                new SingleFileIndex().send(receiver, node, file.getId());
            }
            else{
                List<NodeDTO> t = new ArrayList<NodeDTO>();
                t.add(node);
                tempList.put(file.getId(), t);
            }
        }
        node.setFileIndex(tempList);
        updateFileIndexUI();
    }
    
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
    
    public void createFileIndex(NodeDTO sender, String fileID){
        updateIndex(sender, fileID);
    }
    
    public void updateFileIndex(NodeDTO sender, String fileID){
        int nodeID = idCreator.getComparableID(node.getId());
        String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());
        int senderIntID = idCreator.getComparableID(senderID);
        
        if(rangeChecker.isInRange(senderIntID, nodeID, idCreator.getComparableID(fileID))){
            updateIndex(sender, fileID);
        }
    }
    
    public void updateFileIndexUI(){
        String displayText = "File ID\t\t\tNode\n\t\tIP Address\t\tPort\n\n";
        Map<String, List<NodeDTO>> indices = node.getFileIndex();
        
        for(String fileID : indices.keySet()) {
            displayText += fileID + "\t\t";
            int count = 0;
            for(NodeDTO _node : indices.get(fileID)){
                count++;
                if(count > 1){
                    displayText += "\t\t";
                }
                displayText += _node.getIpAdress() + "\t\t" + _node.getPort() + "\n";
            }
            displayText += "\n";
        }
        mainController.getMainFrame().updateFileIndex(displayText);
    }
    
    private void updateIndex(NodeDTO sender, String fileID) {
        String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());

        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        List<NodeDTO> temp = fileIndex.get(fileID);
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(sender);
            fileIndex.put(fileID, temp);
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
