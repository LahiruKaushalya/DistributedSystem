package CS4262.Core;

import CS4262.Interfaces.IInitializerWordIndex;
import CS4262.Message.WordIndex.AddSingleWordIndex;
import CS4262.Models.File;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.NodeDTO;
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
        Map<String, List<File>> tempList = node.getWordIndex();
        String[] words = getWords(file);
        for(String word : words){
            String wordID = idCreator.generateWordID(word);
            NodeDTO receiver = findReceiver.search(wordID);
            Word _word = new Word(word, wordID);
            if(receiver != null){
                new AddSingleWordIndex().send(new MessageDTO(receiver, _word, file));
            }
            else{
                List<File> temp = tempList.get(word);
                if(temp != null){
                    temp.add(file);
                }
                else{
                    temp = new ArrayList<>();
                    temp.add(file);
                }
                tempList.put(word, temp);
            }
        }
        node.setWordIndex(tempList);
        uiCreator.updateWordIndexUI();
    }
    
    //Add new file to index (incoming msg)
    public void insert(Word word, File file){
        NodeDTO receiver = findReceiver.search(word.getId());
        if (receiver != null) {
            new AddSingleWordIndex().send(new MessageDTO(receiver, word, file));
        } 
        else {
            localAdd(word, file);
        }
    }
    
    //Create file index from predecessor's file insex (incoming msg)
    public void insertFromPredecessor(Word word, File file){
        localAdd(word, file);
    }
    
    //Update when successor changed
    public void updateForSuccessor(){
        Node successor = node.getSuccessor();
        Map<String, List<File>> wordIndex = node.getWordIndex();
        
        int nodeID = idCreator.getComparableID(node.getId());
        int succID = idCreator.getComparableID(successor.getId());
        int wordIntID;
        
        Iterator<Map.Entry<String, List<File>>> iterator = wordIndex.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, List<File>> entry = iterator.next();
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
        return file.getName().toLowerCase().split("_");
    }
    
    private void localAdd(Word word, File file) {
        
        Map<String, List<File>> wordIndex = node.getWordIndex();
        List<File> temp = wordIndex.get(word.getName());
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(file);
            wordIndex.put(word.getName(), temp);
            node.setWordIndex(wordIndex);
        } 
        else {
            boolean exists = false;
            for (File _file : temp) {
                if (_file.getName().equals(file.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                temp.add(file);
                wordIndex.put(word.getName(), temp);
                node.setWordIndex(wordIndex);
            }
        }
    }
    
}
