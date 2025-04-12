package org.example;

import java.util.ArrayList;
import java.util.List;
public class User {
    private String name;
    private String id;
    private List<String> likedMovieIds;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.likedMovieIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getLikedMovieIds() {
        return likedMovieIds;
    }

    public void addLikedMovieId(String movieId) {
        likedMovieIds.add(movieId);
    }

    public void setLikedMovieIds(List<String> likedMovieIds) {
        this.likedMovieIds = likedMovieIds;
    }
}