package CS4262.Core;

import CS4262.Interfaces.IInitializerDownload;
import CS4262.Message.Download.DownloadRequest;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Models.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class DownloadInitializer implements IInitializerDownload{
    
    private static DownloadInitializer instance;

    public static DownloadInitializer getInstance() {
        if (instance == null) {
            instance = new DownloadInitializer();
        }
        return instance;
    }

    private DownloadInitializer() {}
    
    //Initiate download request
    public void downloadFile(String ipAddress, int udpPort, String fileName){
        String fileHolderID = idCreator.generateNodeID(ipAddress, udpPort);
        //Check for local download request
        if(fileHolderID.equals(node.getId())){
            mainController.getMainFrame().displayError("Download request to local node");
        }
        else{
            NodeDTO fileHolder = new NodeDTO(ipAddress, udpPort);
            fileName = fileName.toLowerCase().replace(" ", "_");
            File file = new File(fileName, idCreator.generateFileID(fileName));

            new DownloadRequest().send(new MessageDTO(fileHolder, node, file));
        }
    }
    
    //Send file to requester
    public void uploadFile(NodeDTO requester, File file){
        //Genetate file content
        File generatedFile = genFile(file);
        //Display File content
        uiCreator.displayFileContent(generatedFile);
        
        //Start Tcp server
        
        //Send file
        
        //Stop Tcp server
    }
    
    private File genFile(File file){
        Random ran = new Random();
        int fileSize = ran.nextInt(9) + 2;
        file.setFileSize(fileSize);
        
        fileSize = (fileSize * 1024 ) / 2;
        StringBuilder sb = new StringBuilder(fileSize);
        for (int i = 0; i < fileSize; i++) {
            sb.append(String.valueOf(ran.nextInt(10)));
        }
        
        String base = sb.toString();
        sb = new StringBuilder(1024);
        for (int i = 0; i < 1024; i++) {
            sb.append(base);
        }
        String body = sb.toString();
        String hashCode = genHash(body);
        
        file.setBody(body);
        file.setHashCode(hashCode);
        return file;
    }
    
    //Generate hash for file content
    private String genHash(String fileContent){
        StringBuilder sb = new StringBuilder();
        try {
            //Generate SHA-1 hash
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(fileContent.getBytes());
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DownloadInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
    
}
