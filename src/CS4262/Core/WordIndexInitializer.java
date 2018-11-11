package CS4262.Core;

import CS4262.Interfaces.IInitializerWordIndex;
import CS4262.Message.WordIndex.*;
import CS4262.Models.*;
import CS4262.Models.DataTransfer.*;
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

    private WordIndexInitializer() {}
    
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
                List<File> files = tempList.get(word);
                if (files != null) {
                    if (!isExists(files, file)) {
                        files.add(file);
                    }
                }
                else{
                    files = new ArrayList<>();
                    files.add(file);
                }
                tempList.put(word, files);
            }
        }
        node.setWordIndex(tempList);
        uiCreator.updateWordIndexUI();
    }
    
    //Create word index from predecessor's word insex (incoming msg)
    public void updateFromPre(Word word, File file){
        Node successor = node.getSuccessor();
        if(successor != null){
            int sucID = idCreator.getComparableID(successor.getId());
            int nodeID = idCreator.getComparableID(node.getId());
            int wordIntID = idCreator.getComparableID(word.getId());
            if(!rangeChecker.isInRange(nodeID, sucID, wordIntID)){
                localAdd(word, file);
            }
        }
        else{
            localAdd(word, file);
        }
    }
    
    //Modify word index when successor leaving (incoming msg)
    public void updateFromSuc(Word word, File file){
        localAdd(word, file);
    }
    
    //Activate word index backup when sucessor crash
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
        
        NodeDTO predecessor = node.getPredecessor();
        if(predecessor != null){
            new BackupWordIndex().send(new MessageDTO(node.getPredecessor()));
        }
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
    
    //Update when successor changed
    public void updateWhenSucChanged(){
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
        
        NodeDTO predecessor = node.getPredecessor();
        if(predecessor != null){
            new BackupWordIndex().send(new MessageDTO(predecessor));
        }
    }
    
    private String[] getWords(File file){
        return file.getName().split("_");
    }
    
    private void localAdd(Word word, File file) {
        Map<String, List<File>> wordIndex = node.getWordIndex();
        List<File> files = wordIndex.get(word.getName());
        
        if (files == null) {
            files = new ArrayList<>();
            files.add(file);
        } 
        else {
            if (!isExists(files, file)) {
                files.add(file);
            }
        }
        wordIndex.put(word.getName(), files);
        node.setWordIndex(wordIndex);
    }
    
    private boolean isExists(List<File> files, File file){
        for (File _file : files) {
            if (_file.getId().equals(file.getId())) {
                return true;
            }
        }
        return false;
    }
    
}
