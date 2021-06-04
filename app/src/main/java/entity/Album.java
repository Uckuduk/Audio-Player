package entity;

import java.io.Serializable;

public class Album implements Serializable {
    private String cover_xl;

    public Album(String cover) {
        this.cover_xl = cover;
    }

    public String getCover() {
        return cover_xl;
    }

    public void setCover(String cover) {
        this.cover_xl = cover;
    }
}
