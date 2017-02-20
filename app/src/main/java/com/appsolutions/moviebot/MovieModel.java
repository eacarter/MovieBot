package com.appsolutions.moviebot;

public class MovieModel {

    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String actor;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private String metascore;
    private String imdbrating;
    private String imdbvotes;
    private String imdbid;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setImdbid(String imdbid) {
        this.imdbid = imdbid;
    }

    public void setImdbvotes(String imdbvotes) {
        this.imdbvotes = imdbvotes;
    }

    public void setImdbrating(String imdbrating) {
        this.imdbrating = imdbrating;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    @Override
    public String toString(){
         return "Title: " +title + "\n" +
                "Year: " + year + "\n" +
                "Rated: " + rated + "\n" +
                "Released: " + released + "\n" +
                "Runtime: " + runtime + "\n" +
                "Genre: " + genre + "\n" +
                "Director(s): " + director + "\n" +
                "Actor(s): " + actor + "\n" +
                "Plot: " + plot;
    }


}
