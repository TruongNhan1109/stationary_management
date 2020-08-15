package vn.com.ttn.mymusic;

public class Song {

    private int idSong;
    private String nameSong;
    private  int file;

    public Song () {

    }

    public Song(int idSong,String name, int file) {
        this.idSong = idSong;
        this.nameSong = name;
        this.file = file;
    }




    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }
}
