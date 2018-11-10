package CS4262.Core;

import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Models.DataTransfer.SearchDTO;
import CS4262.Models.DataTransfer.MessageDTO;
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

    private SearchInitializer() {}
    
    //Starts serarh from search term
    public void localSearch(String fileName){
        //Clear previous results
        clearSearchResults();
        
        if(node != null){
            //Filter user input
            SearchDTO searchDTO = filterFileName(fileName);
            
            String keyWord = searchDTO.getWord().getName();
            //Check in local wordIndex for keyword
            boolean isKeyAvailble = isWordAvailable(keyWord);
            if (isKeyAvailble) {
                //KeyWord locally available. Start File search
                startFileSearch(node, searchDTO);
            }
            else{
                findWordRedirector(node, searchDTO);
            }
        }
    }
    
    //File Search
    public void globalSearch(NodeDTO sender, File file) {
        String senderId = idCreator.generateNodeID(sender.getipAdress(), sender.getUdpPort());
        List<SearchResult> searchResults = getResults(file);
        if(senderId.equals(node.getId()) && searchResults != null){
            node.setSearchResults(searchResults);
            uiCreator.updateSearchResultsUI();
        }
        else{
            //Check at least one file holder has been found
            if (searchResults != null) {
                new SearchResults().send(new MessageDTO(sender, searchResults));
            } //File holder not found. 
            else {
                //Check for a search redirector node
                findFileRedirector(sender, file);
            }
        }
    }
    
    //Word search
    public void globalSearch(NodeDTO sender, SearchDTO searchDTO){
        String keyWord = searchDTO.getWord().getName();
        //Check in local wordIndex for keyword
        boolean isKeyAvailble = isWordAvailable(keyWord);
        if (isKeyAvailble) {
            //KeyWord locally available
            startFileSearch(sender, searchDTO);
        } else {
            findWordRedirector(sender, searchDTO);
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
            //Find nearest node to content 
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
    private void startFileSearch(NodeDTO sender, SearchDTO searchDTO){
        Word searchingWord = searchDTO.getWord();
        File searchingFile = searchDTO.getFile();
        
        List<File> files = node.getWordIndex().get(searchingWord.getName());
        boolean isFileMatched = false;
        
        for(File file : files){
            if(file.getId().equals(searchingFile.getId())){
                globalSearch(sender, file);
                isFileMatched = true;
                break;
            }
        }
        
        if(!isFileMatched){
            for (File file : files) {
                globalSearch(sender, file);
            }
        }
        
    }
    
    //Chaeck word available in word index
    private boolean isWordAvailable(String keyWord){
        Map<String, List<File>> wordIndex = node.getWordIndex();
        for (String word : wordIndex.keySet()) {
            if (word.equals(keyWord)) {
                return true;
            }
        }
        return false;
    }
    
    private List<SearchResult> getResults(File file){
        //Check searching file index availability
        Map<String, List<NodeDTO>> index = node.getFileIndex();
        List<SearchResult> results = new ArrayList<>();
        List<NodeDTO> fileHolders = null;
        
        for (String fileID : index.keySet()) {
            if (fileID.equals(file.getId())) {
                fileHolders = index.get(fileID);
                break;
            }
        }
        
        if(fileHolders != null){
            for(NodeDTO fileHolder : fileHolders){
                results.add(new SearchResult(file, fileHolder));
            }
            return results;
        }
        return null;
    }
    
    private SearchDTO filterFileName(String fileName) {
        //Implement filter logic
        fileName =  fileName.trim().replace(" ", "_");
        
        String[] words = fileName.split("_");
        String keyWord = words[0];
        
        Word word = new Word(keyWord, idCreator.generateWordID(keyWord.toLowerCase()));
        File file = new File(fileName, idCreator.generateFileID(fileName.toLowerCase()));
        
        return new SearchDTO(word, file);
    }
    
    private void clearSearchResults(){
        List<SearchResult> searchResults = node.getSearchResults();
        searchResults.clear();
        node.setSearchResults(searchResults);
    }
    
}
