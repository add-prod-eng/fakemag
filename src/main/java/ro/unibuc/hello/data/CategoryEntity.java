package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;

public class CategoryEntity {

    @Id
    private String id;
    
    private String name;
    private String description;

    public CategoryEntity() {}

    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CategoryEntity(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "Category[id='%s', name='%s', description='%s']",
                id, name, description);
    }
}
