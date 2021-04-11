package entity;

import java.io.Serializable;

public class Data implements Serializable{

    private String title_short;
    private String preview;
    public Artist artist;
    public Album album;

    public Data(String title_short, String preview){
        this.title_short = title_short;
        this.preview = preview;
    };

    public void setPicture(String picture){ this.album.setCover(picture);}

    public void setArtist(String name) {
        this.artist.setName(name);
    }

    public void setTitle_short(String title_short) {
        this.title_short = title_short;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPicture(){ return album.getCover();}

    public String getArtist() {
        return artist.getName();
    }

    public String getTitle_short() {
        return title_short;
    }

    public String getPreview() {
        return preview;
    }


}
