package CS4262.Models;

/**
 *
 * @author Lahiru Kaushalya
 */
public class File {
    
    private final String name;
    private final String id;
    private String body;
    private String hashCode;

    public File(String name, String id){
        this.name = name;
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
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
