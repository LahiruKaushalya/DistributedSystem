package CS4262.Helpers;

import CS4262.Core.NodeInitializer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class IDCreator {
    
    private final int BIN_ID_LENGTH;

    public int getBIN_ID_LENGTH() {
        return BIN_ID_LENGTH;
    }

    public IDCreator(){
        this.BIN_ID_LENGTH = 10;
    }
    
    public String generateNodeID(String ip, int port) {
        String data = ip + String.valueOf(port);
        return "N" + String.valueOf(generateID(data));
    }
    
    public String generateFileID(String fileName) {
        return "F" + String.valueOf(generateID(fileName));
    }
    
    public int getComparableID(String stringID) {
        return Integer.parseInt(stringID.substring(1, stringID.length()));
    }
    
    private int generateID(String data){
        int id = 0;
        try {
            //Generate SHA-1 hash
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Convert Hex string to 160 bit binary string
            String binString = hexToBinString(sb.toString());
            //create m bit id
            String binID = binString.substring(0, BIN_ID_LENGTH);
            //Convert binary id to decimal id
            id = Integer.parseInt(binID, 2);
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(IDCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    private String hexToBinString(String hexString) {
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
