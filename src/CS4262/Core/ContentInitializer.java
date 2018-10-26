package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class ContentInitializer {
    
    private final String[] availableFiles;
    private final IDCreator idCreator;
    private final Node node;
    private final MainController mainController;
    
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
        this.idCreator = new IDCreator();
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
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
    }
    
    public boolean updateFileIndex(NodeDTO sender, StringTokenizer st) {
        boolean same = false;
        int noOfFiles = Integer.parseInt(st.nextToken());
        String nodeID = node.getId();
        
        Node successor = node.getSuccessor();
        Map<String, List<NodeDTO>> fileIndex = node.getFileIndex();
        
        int nodeIntID = idCreator.getComparableID(nodeID);
        int succIntID = idCreator.getComparableID(successor.getId());
            
        for (int i = 0; i < noOfFiles; i++) {
            String fileID = st.nextToken();
            int fileIntID = idCreator.getComparableID(fileID);
            
            List<NodeDTO> index;
            if (isInRange(succIntID, nodeIntID, fileIntID)) {
                if (fileIndex.containsKey(fileID)) {
                    index = fileIndex.get(fileID);
                    String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());
                    boolean exists = false;
                    for(NodeDTO _node : index){
                        String _nodeID = idCreator.generateNodeID(_node.getIpAdress(), _node.getPort());
                        if(senderID.equals(_nodeID)){
                            exists = true;
                            same = true;
                            break;
                        }
                    }
                    if(!exists){
                        index.add(sender);
                        fileIndex.put(fileID, index);
                        same = false;
                    }
                } 
                else {
                    index = new ArrayList<>();
                    index.add(sender);
                    fileIndex.put(fileID, index);
                    same = false;
                }
            }
        }
        
        for(String fileID : fileIndex.keySet()){
            int fileIntID = idCreator.getComparableID(fileID);
            if(!isInRange(succIntID, nodeIntID, fileIntID)){
                fileIndex.remove(fileID);
                same = false;
            }
        }
        if(!same){
            //Update node file index
            node.setFileIndex(fileIndex);
            //Update file index UI
            updateFileIndexUI();
        }
        return same;
    }
    
    private void updateFileIndexUI(){
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
    
    private boolean isInRange(int lowerbound, int upperbound, int value){
        
        int m = idCreator.getBIN_ID_LENGTH();
        int bp = (int)Math.pow(2, m);
        
        if(lowerbound == 0){
            return upperbound <= value &&  value <= bp;
        }
        //0 within the range
        else if(lowerbound < upperbound){
            return (0 <= value && value < lowerbound) || (upperbound <= value && value < bp);
        }
        //0 outside the range
        else{
            return upperbound <= value && value < lowerbound;
        }
        
    }

}
