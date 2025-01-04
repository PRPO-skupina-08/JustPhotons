package si.justphotons.coordinator.entities.external;

public class Album {

    private Long id;
    private String title;
    private String titleImage;  
      
    public  Album() {}
    
    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
