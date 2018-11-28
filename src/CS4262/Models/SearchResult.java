package CS4262.Models;

import CS4262.Models.DataTransfer.NodeDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchResult {
    
    private final File file;
    private final NodeDTO fileHolder;
    private final int hopCount;
    
    public SearchResult(File file, NodeDTO fileHolder, int hopCount) {
        this.file = file;
        this.fileHolder = fileHolder;
        this.hopCount = hopCount;
    }

    public File getFile() {
        return file;
    }

    public NodeDTO getFileHolder() {
        return fileHolder;
    }

    public int getHopCount() {
        return hopCount;
    }
    
}
