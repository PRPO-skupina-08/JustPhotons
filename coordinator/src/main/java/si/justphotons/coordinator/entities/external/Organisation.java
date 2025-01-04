package si.justphotons.coordinator.entities.external;

import java.util.ArrayList;
import java.util.List;

public class Organisation {

    private Long id;

    private String name;

    private String description;

    private List<Album> albums = new ArrayList<>();
    
    public  Organisation() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}