package CS4262.Core;

import CS4262.Interfaces.IInitializerWordIndex;
import static CS4262.Interfaces.IInitializerWordIndex.wordIndexInitializer;
import static CS4262.Interfaces.IMain.findReceiver;
import static CS4262.Interfaces.IMain.idCreator;
import static CS4262.Interfaces.IMain.node;
import static CS4262.Interfaces.IMain.rangeChecker;
import static CS4262.Interfaces.IMain.uiCreator;
import CS4262.Message.FileIndex.AddSingleFileIndex;
import CS4262.Message.WordIndex.AddSingleWordIndex;
import CS4262.Models.File;
import CS4262.Models.MessageDTO;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import CS4262.Models.Word;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lahiru Kaushalya
 */
public class WordIndexInitializer implements IInitializerWordIndex{
    
    private static WordIndexInitializer instance;

    public static WordIndexInitializer getInstance() {
        if (instance == null) {
            instance = new WordIndexInitializer();
        }
        return instance;
    }

    private WordIndexInitializer() {
        
    }
    
    public void createLocalWordIndex(File file){
        Map<String, List<String>> tempList = node.getWordIndex();
        String[] words = getWords(file);
        for(String word : words){
            String wordID = idCreator.generateWordID(word);
            NodeDTO receiver = findReceiver.search(wordID);
            Word _word = new Word(word, wordID);
            if(receiver != null){
                new AddSingleWordIndex().send(new MessageDTO(receiver, _word, file.getId()));
            }
            else{
                List<String> temp = new ArrayList<>();
                temp.add(file.getId());
                tempList.put(word, temp);
            }
        }
        node.setWordIndex(tempList);
        uiCreator.updateWordIndexUI();
    }
    
    //Add new file to index (incoming msg)
    public void insert(Word word, String fileID){
        NodeDTO receiver = findReceiver.search(word.getId());
        if (receiver != null) {
            new AddSingleWordIndex().send(new MessageDTO(receiver, word, fileID));
        } 
        else {
            localAdd(word, fileID);
        }
    }
    
    //Create file index from predecessor's file insex (incoming msg)
    public void insertFromPredecessor(Word word, String fileID){
        localAdd(word, fileID);
    }
    
    //Update when successor changed
    public void updateForSuccessor(){
        Node successor = node.getSuccessor();
        Map<String, List<String>> wordIndex = node.getWordIndex();
        
        int nodeID = idCreator.getComparableID(node.getId());
        int succID = idCreator.getComparableID(successor.getId());
        int wordIntID;
        
        Iterator<Map.Entry<String, List<String>>> iterator = wordIndex.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            String wordName = entry.getKey();
            wordIntID = idCreator.getComparableID(idCreator.generateWordID(wordName));
            
            if(rangeChecker.isInRange(nodeID, succID, wordIntID)){
                iterator.remove();
            }
        }
        node.setWordIndex(wordIndex);
        uiCreator.updateWordIndexUI();
    }
    
    private String[] getWords(File file){
        return file.getName().split(" ");
    }
    
    private void localAdd(Word word, String fileID) {
        
        Map<String, List<String>> wordIndex = node.getWordIndex();
        List<String> temp = wordIndex.get(word.getId());
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(fileID);
            wordIndex.put(word.getName(), temp);
            node.setWordIndex(wordIndex);
        } 
        else {
            boolean exists = false;
            for (String _fileID : temp) {
                if (_fileID.equals(fileID)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                temp.add(fileID);
                wordIndex.put(word.getName(), temp);
                node.setWordIndex(wordIndex);
            }
        }
    }
    
}
