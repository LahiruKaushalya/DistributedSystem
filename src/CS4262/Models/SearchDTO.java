package CS4262.Models;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchDTO {
    
    private final Word word;
    private final File file;
    
    public SearchDTO(Word word, File file){
        this.word = word;
        this.file = file;
    }

    public Word getWord() {
        return word;
    }

    public File getFile() {
        return file;
    }
}
