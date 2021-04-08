package entity;

public class PlayList {
    private Data[] tracks;

    public PlayList(){tracks = new Data[0];}

    public PlayList(Data[] tracks){
        this.tracks = tracks;
    }

    public int count(){return tracks.length;}

    public Data get(int index){
        if(index < tracks.length && index >= 0) {
            return tracks[index];
        }
        else
            return null;
    }

    public void appendSong(Data track){
        Data[] array = new Data[tracks.length + 1];

        for(int i = 0; i < tracks.length; i++){
            array[i] = tracks[i];
        }

        array[array.length - 1] = track;

        tracks = array;
    }
}
