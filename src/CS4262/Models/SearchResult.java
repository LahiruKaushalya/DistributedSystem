package CS4262.Models;

import CS4262.Models.DataTransfer.NodeDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchResult {
    
    private final File file;
    private final NodeDTO fileHolder;

    public SearchResult(File file, NodeDTO fileHolder) {
        this.file = file;
        this.fileHolder = fileHolder;
    }

    public File getFile() {
        return file;
    }

    public NodeDTO getFileHolder() {
        return fileHolder;
    }
    
}
