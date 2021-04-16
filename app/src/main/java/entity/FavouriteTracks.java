package entity;

import com.example.myapplication.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class FavouriteTracks {
    public static ArrayList<Integer> favouriteIds;
    public static PlayList tracks;

    public static void addFavourite(Data track){
        favouriteIds.add(track.getId());
        tracks.appendSong(track);
    }

    public static void deleteFavourite(Data track){
        Iterator<Integer> iterator = favouriteIds.iterator();

        while (iterator.hasNext()){
            Integer id = iterator.next();
            if(id.equals(track.getId())){
                iterator.remove();
            }
        }

        tracks.deleteSong(track);
    }


}
