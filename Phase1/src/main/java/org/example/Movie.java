package org.example;

public class Movie {
    private String title;
    private String id;
    private String[] genres;

    public Movie(String title, String id, String[] genres) {
        this.title = title;
        this.id = id;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String[] getGenres() {
        return genres;
    }

    public boolean hasGenre(String genre) {
        for (String g : genres) {
            if (g.trim().equalsIgnoreCase(genre.trim())) {
                return true;
            }
        }
        return false;
    }
}