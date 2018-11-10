package CS4262.Core;

import CS4262.Models.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import CS4262.Interfaces.IInitializerContent;

/**
 *
 * @author Lahiru Kaushalya
 */
public class ContentInitializer implements IInitializerContent {
    
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
    
    //Create content
    public void createNodeContent(){
        
        Random ran = new Random();
        int numOfFiles = 3 + ran.nextInt(3);
        
        List<File> content = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        
        //genatate random number of files from available files
        String fileName;
        int index;
        
        while(numOfFiles > 0){
            index = ran.nextInt(availableFiles.length - 1);
            //Avoid file duplications
            if(!temp.contains(index)){
                fileName = availableFiles[index].toLowerCase().replace(" ", "_");
                String fileID = idCreator.generateFileID(fileName);
                content.add(new File(fileName, fileID));
                temp.add(index);
                numOfFiles--;
            }
        }
        //set node content
        node.setContent(content);
        //update UI
        uiCreator.updateContentUI();
        
        //Create file index
        FileIndexInitializer.getInstance().createLocalFileIndex();
        
    }
    
    //Delete content
    public void removeContent(){
        List<File> content = node.getContent();
        content.clear();
        node.setContent(content);
        
        //update UI
        String text = "File ID\tFile Name\n\n";
        mainController.getMainFrame().updateContent(text);
    }
    
}
