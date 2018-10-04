package CS4262;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */

//This will recieve all of the available nodes in the bootstrap server

public class NodeInitializer {
    
    private final Node node;
    private final MainController mc;
    private static int ID_LENGTH;
    
    public NodeInitializer(){
        this.mc = MainController.getInstance();
        this.node = mc.getNode();
        this.ID_LENGTH = 8;
    }
    
    /**
     * 
     * @param nodes 
     */
    public void initializeNode(ArrayList<NodeDTO> nodes) 
    {
        System.out.print(nodes);
    }
    
    public static String generateNodeID(String ip, int port){
        String id = "";
        try {
            String data = ip + String.valueOf(port);
            
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //Add password bytes to digest
            md.update(data.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String encripted = hexToBinString(sb.toString());
            id = encripted.substring(0, ID_LENGTH);
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NodeInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return id;
        }
    }
    
    private static String hexToBinString(String hexString) {
        String binString = "";
        hexString = hexString.trim();
        hexString = hexString.replaceFirst("0x", "");
        
        String binFragment;
        int intHex;
        
        for (int i = 0; i < hexString.length(); i++) {
            intHex = Integer.parseInt(String.valueOf(hexString.charAt(i)), 16);
            binFragment = Integer.toBinaryString(intHex);

            while (binFragment.length() < 4) {
                binFragment = "0" + binFragment;
            }
            binString += binFragment;
        }
        return binString;
    }
    
}
