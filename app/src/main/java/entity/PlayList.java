package entity;

public class PlayList {
    private Data[] tracks;

    public PlayList() {
        tracks = new Data[0];
    }

    public PlayList(Data[] tracks) {
        this.tracks = tracks;
    }

    public int count() {
        return tracks.length;
    }

    public Data get(int index) {
        if (index < tracks.length && index >= 0) {
            return tracks[index];
        } else
            return null;
    }

    public void deleteSong(Data track) {
        boolean deleted = false;
        Data[] array = new Data[tracks.length - 1];
        int i = 0;

        for (; i < array.length; i++) {
            if (!deleted) {
                if (tracks[i].getId() != track.getId()) {
                    array[i] = tracks[i];
                }
            }
            else
                deleted = true;
                array[i] = tracks[i+1];

        }

        tracks = array;
    }

    public void appendSong(Data track) {
        Data[] array = new Data[tracks.length + 1];

        for (int i = 0; i < tracks.length; i++) {
            array[i] = tracks[i];
        }

        array[array.length - 1] = track;

        tracks = array;
    }
}
