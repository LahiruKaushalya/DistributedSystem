package CS4262.Core;

import CS4262.MainController;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Network.MessageSender;
import CS4262.Models.NodeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Lahiru Kaushalya
 */
//This will recieve 2 random nodes in the bootstrap server
public class NodeInitializer {
    
    private final Node node;
    private final MessageSender msgHandler;
    private final MainController mainController;
    private final RouteInitializer routeInitializer;
    private static int hopCount;
    
    public NodeInitializer() {
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.msgHandler = MessageSender.getInstance();
        this.routeInitializer = new RouteInitializer();
        NodeInitializer.hopCount = 4;
    }

    public static int getHopCount() {
        return hopCount;
    }

    /**
     *
     * @param newNodes
     */
    public void initializeNode(ArrayList<NodeDTO> newNodes) {
        
        //Create node content
        createContent();
        
        String response;
        if (newNodes != null) {
            for(NodeDTO neighbour : newNodes){
                response = msgHandler.join(neighbour, node, hopCount);
                routeInitializer.addAndUpdate(neighbour);
                /*
                Handle response here
                */
            }
            routeInitializer.updateRoutesUI();
        }
        
        Node[] neighbours = node.getRoutes();
        for(Node neighbour : neighbours){
            if(neighbour != null){
                response = msgHandler.updateRoutes(neighbour, node, null, hopCount);
                /*
                Handle response here
                */
            }
        }
    }
    
    private void createContent(){
        
        String[] availableFileNames = new String[]{
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
        
        Random ran = new Random();
        int numOfFiles = 3 + ran.nextInt(3);
        
        List<File> content = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        
        String text = "File Names\n\n",fileName;
        
        //genatate random number of files from available files
        int x = 0, index;
        while(x < numOfFiles){
            index = ran.nextInt(availableFileNames.length - 1);
            //Avoid file duplications
            if(!temp.contains(index)){
                fileName = availableFileNames[index];
                content.add(new File(fileName));
                text += fileName + "\n";
                temp.add(index);
                x++;
            }
        }
        //set node content
        node.setContent(content);
        
        //update UI
        mainController.getMainFrame().updateContent(text);
    }
    
}
