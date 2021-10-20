package doryanbessiere.procopy.fr.configuration.user;

public enum Directories {

    DESKTOP("Desktop", "Bureau"),
    VIDEOS("Videos", "Vidéos"),
    PICTURES("Pictures", "Images"),
    DOWNLOADS("Downloads", "Téléchargements"),
    OBJECTS_3D("3D Objects", "3D Objects"),
    MUSIC("Music", "Musique"),
    CONTACTS("Contacts", "Contacts"),
    LINKS("Links", "Liens"),
    FAVORITES("Favorites", "Favorites"),
    SEARCHS("Searchs", "Recherches"),
    SAVED_GAMES("Saved Games", "Partie sauvegardées");

    private String name;
    private String fr;

    Directories(String name, String fr) {
        this.name = name;
        this.fr = fr;
    }

    public String getFr() {
        return fr;
    }

    public String getName() {
        return name;
    }
}
