package servlet;

public class Group {
    private Integer id;
    private String name;
    private String domain;

    private static Group ourInstance = new Group();
    public static Group getInstance() {
        return ourInstance;
    }

    private Group() {
    }

    public Group(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    @Override
    public String toString() {
        return "{name="+name+",ID="+id.toString()+"}";
    }
}