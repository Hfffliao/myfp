package love.linyi.domin;

public class UserFolder {
    private int id;
    private String path;
    private String name;
    private String type;
    private int userId;



    public UserFolder(int id, String name, String path, String type, int userId) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.userId = userId;
    }




    public UserFolder() {
    }
    public UserFolder(int id, String name, String path, String type) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
