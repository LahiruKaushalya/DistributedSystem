package CS4262.Models;

/**
 *
 * @author Lahiru Kaushalya
 */
public class File {
    
    private final String name;
    private String body;
    private String hashCode;

    public File(String name){
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
