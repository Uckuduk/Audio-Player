package entity;

public class PlayList {
    private Track[] tracks;

    public PlayList(){tracks = new Track[0];}

    public PlayList(Track[] tracks){
        this.tracks = tracks;
    }

    public int count(){return tracks.length;}

    public Track get(int index){
        if(index < tracks.length && index >= 0) {
            return tracks[index];
        }
        else
            return null;
    }

    public void appendSong(Track track){
        Track[] array = new Track[tracks.length + 1];

        for(int i = 0; i < tracks.length; i++){
            array[i] = tracks[i];
        }

        array[array.length - 1] = track;

        tracks = array;
    }
}
