package CS4262.Core;

import CS4262.Message.Search.*;
import CS4262.Models.*;
import CS4262.Interfaces.IInitializerSearch;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchInitializer implements IInitializerSearch{

    private static SearchInitializer instance;

    public static SearchInitializer getInstance() {
        if (instance == null) {
            instance = new SearchInitializer();
        }
        return instance;
    }

    private SearchInitializer() {
        
    }
    
    public void localSearch(String fileName){
        if(node != null){
            //Filter user input
            SearchDTO searchDTO = filterFileName(fileName);
            
            String keyWord = searchDTO.getWord().getName();
            //Check in local wordIndex for keyword
            boolean isKeyAvailble = isWordAvailable(keyWord);
            if (isKeyAvailble) {
                //KeyWord locally available
                searchFile(node, searchDTO);
            }
            else{
                findWordRedirector(node, searchDTO);
            }
        }
    }
    
    public void globalSearch(NodeDTO sender, File file) {
        String searchingFileID = file.getId();
        
            //Search locally first
            List<NodeDTO> fileHolders = getFileHolders(searchingFileID);
            //Check in local files
            boolean isAvailble = isFileAvailable(searchingFileID);
            if (isAvailble) {
                //File locally available
                boolean exists = false;
                if(fileHolders != null){
                    for(NodeDTO fileHolder : fileHolders){
                        String fhID = idCreator.generateNodeID(fileHolder.getIpAdress(), fileHolder.getPort());
                        if(fhID.equals(node.getId())){
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        fileHolders.add(new NodeDTO(node.getIpAdress(), node.getPort()));
                    }
                }
                else {
                    fileHolders = new ArrayList<>();
                    fileHolders.add(new NodeDTO(node.getIpAdress(), node.getPort()));
                }
            }
            //Check at least one file holder has been found
            if (fileHolders != null) {
                new SearchResults().send(new MessageDTO(sender, fileHolders));
            } 
            //File holder not found. 
            else {
                //Check for a search redirector node
                findFileRedirector(sender, file);
            }
        
    }
    
    public void globalSearch(NodeDTO sender, SearchDTO searchDTO){
        String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());
        
        if (!senderID.equals(node.getId())) {
            
        }
    }
     
    private void findFileRedirector(NodeDTO sender, File file){
        int fileID = idCreator.getComparableID(file.getId());
        NodeDTO redirector = FindRedirector(fileID);
        
        if(redirector != null){
            new FileSearchRequest().send(new MessageDTO(redirector, sender, file));
        }
        else{
            //File not found
        }
    }
    
    private void findWordRedirector(NodeDTO sender, SearchDTO searchDTO){
        int wordID = idCreator.getComparableID(searchDTO.getWord().getId());
        NodeDTO redirector = FindRedirector(wordID);
        
        if(redirector != null){
            new WordSearchRequest().send(new MessageDTO(redirector, sender, searchDTO));
        }
        else{
            //File not found
        }
    }
    
    private NodeDTO FindRedirector(int id) {
        Node redirector = null;
        Node[] neighbours = node.getRoutes();
        
        if (neighbours != null) {
            int nodeIntID = idCreator.getComparableID(node.getId());
            Node neighbour;
            int neighbourIntID;
            //Find nearest node to file 
            for (int i = neighbours.length - 1; i >= 0; i--) {
                neighbour = neighbours[i];
                if (neighbour != null) {
                    neighbourIntID = idCreator.getComparableID(neighbour.getId());
                    if (rangeChecker.isInRange(nodeIntID, neighbourIntID, id)) {
                        redirector = neighbour;
                        break;
                    }
                }
            }
        }
        return redirector;
    }
       
    //This method will call when searching keyword available in local word index
    private void searchFile(NodeDTO sender, SearchDTO searchDTO){
        String senderId = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());
        Word searchingWord = searchDTO.getWord();
        File searchingFile = searchDTO.getFile();
        
        List<File> files = node.getWordIndex().get(searchingWord.getName());
        boolean isFileMatched = false;
        
        for(File file : files){
            if(file.getId().equals(searchingFile.getId())){
                if(!senderId.equals(node.getId())){
                    globalSearch(sender, file);
                }
                else{
                    mainController.getMainFrame().displayError(file.getName());
                }
                isFileMatched = true;
                break;
            }
        }
        
        if(!isFileMatched){
            for (File file : files) {
                if (!senderId.equals(node.getId())) {
                    globalSearch(sender, file);
                } 
                else {
                    mainController.getMainFrame().displayError(file.getName());
                }

            }
        }
        
    }
    
    private boolean isWordAvailable(String keyWord){
        Map<String, List<File>> wordIndex = node.getWordIndex();
        for (String word : wordIndex.keySet()) {
            if (word.equals(keyWord)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isFileAvailable(String searchingFileID){
        List<File> files = node.getContent();
        for (File file : files) {
            if (file.getId().equals(searchingFileID)) {
                return true;
            }
        }
        return false;
    }
    
    private List<NodeDTO> getFileHolders(String searchingFileID){
        //Check searching file index availability
        Map<String, List<NodeDTO>> index = node.getFileIndex();
        for (String fileID : index.keySet()) {
            if (fileID.equals(searchingFileID)) {
                return index.get(fileID);
            }
        }
        return null;
    }
    
    private SearchDTO filterFileName(String fileName) {
        //Implement filter logic
        fileName =  fileName.trim().toLowerCase().replace(' ', '_');
        
        String[] words = fileName.split("_");
        String keyWord = words[0];
        
        Word word = new Word(keyWord, idCreator.generateWordID(keyWord));
        File file = new File(fileName, idCreator.generateFileID(fileName));
        
        return new SearchDTO(word, file);
    }
    
}
