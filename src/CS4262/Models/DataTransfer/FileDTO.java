package CS4262.Models.DataTransfer;

import CS4262.Models.File;
import java.io.Serializable;

/**
 *
 * @author Sankaja
 */
public class FileDTO implements Serializable {

    private File fileObject;
    private NodeDTO sender;
    private final NodeDTO receiver;

    public FileDTO(NodeDTO receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the fileObject
     */
    public File getFileObject() {
        return fileObject;
    }

    /**
     * @param fileObject the fileObject to set
     */
    public void setFileObject(File fileObject) {
        this.fileObject = fileObject;
    }

    /**
     * @return the sender
     */
    public NodeDTO getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(NodeDTO sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public NodeDTO getReceiver() {
        return receiver;
    }

}
