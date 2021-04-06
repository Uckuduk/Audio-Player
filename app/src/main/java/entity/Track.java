package entity;

import java.io.Serializable;

public class Track implements Serializable {
    private String artist;
    private String name;
    private String url;

    public Track(String artist, String name, String url){
        this.artist = artist;
        this.name = name;
        this.url = url;
    };

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "TrackInfo{" +
                "artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
