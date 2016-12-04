package com.themoviedb.model;

/**
 * Created by ILM on 5/10/2016.
 */
public class Trailer {
    public final String key;
    public final String name;
    public final String site;
    public final String type;

    public Trailer(String key, String name, String site, String type) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }
}
