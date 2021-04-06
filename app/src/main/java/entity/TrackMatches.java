package entity;

public class TrackMatches{
    public  Track[] track;

    public Track getTrack(int index) {
        return track[index];
    }

    public void setTrack(Track[] track) {
        this.track = track;
    }
}