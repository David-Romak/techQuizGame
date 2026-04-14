package com.romac.techquizgame.model;

import java.util.List;

/**
 * Represents a single technology in the quiz.
 * Hey, what we're doing here is: a simple POJO (Plain Old Java Object) that
 * Jackson (Spring's JSON library) will automatically serialize to JSON
 * when we return it from our REST endpoints.
 */
public class Tech {

    private String name;
    private String iconUrl;
    private List<String> aliases; // accepted alternate answers e.g. "js" for "JavaScript"
    private String category;

    public Tech() {}

    public Tech(String name, String iconUrl, List<String> aliases, String category) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.aliases = aliases;
        this.category = category;
    }

    // --- Getters & Setters (Spring/Jackson needs these) ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public List<String> getAliases() { return aliases; }
    public void setAliases(List<String> aliases) { this.aliases = aliases; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
