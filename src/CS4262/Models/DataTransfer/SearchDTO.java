package CS4262.Models.DataTransfer;

import CS4262.Models.File;
import CS4262.Models.Word;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchDTO {
    
    private final Word word;
    private final File file;
    private int hopCount;
    
    public SearchDTO(File file, int hopCount){
        this.file = file;
        this.word = null;
        this.hopCount = hopCount;
    }
    
    public SearchDTO(Word word, File file, int hopCount){
        this.word = word;
        this.file = file;
        this.hopCount = hopCount;
    }
    
    public Word getWord() {
        return word;
    }

    public File getFile() {
        return file;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void incHopCount() {
        this.hopCount++;
    }
    
}
