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

    public NodeInitializer() {
        this.mc = MainController.getInstance();
        this.node = mc.getNode();
        this.ID_LENGTH = 8;
    }

    /**
     *
     * @param nodes
     */
    public void initializeNode(ArrayList<NodeDTO> nodes) {

        if (nodes != null) {
            int nodeID = getComparableID(node.getId());
            int neighbourintID;
            int successorID = nodeID;
            int predecessorID = nodeID;
            String neighbourID;

            for (NodeDTO neighbour : nodes) {
                neighbourID = generateNodeID(neighbour.getIpAdress(), neighbour.getPort());
                neighbourintID = getComparableID(neighbourID);
                if (successorID < neighbourintID) {
                    successorID = neighbourintID;
                    node.setSuccessor(new Node(neighbour.getIpAdress(), neighbour.getPort(), "", neighbourID));
                } 
                else if (predecessorID > neighbourintID) {
                    predecessorID = neighbourintID;
                    node.setPredecessor(new Node(neighbour.getIpAdress(), neighbour.getPort(), "", neighbourID));
                }
            }
        }
        System.out.print(nodes);
    }

    public static String generateNodeID(String ip, int port) {
        String id = "";
        try {
            String data = ip + String.valueOf(port);

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

            //Trancate into n bits and create binary id
            String binID = binString.substring(0, ID_LENGTH);

            //Convert binary id to decimal id
            int decId = Integer.parseInt(binID, 2);
            id = "N" + String.valueOf(decId);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NodeInitializer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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

    private int getComparableID(String idString) {
        return Integer.parseInt(idString.substring(1, idString.length()));
    }
}
