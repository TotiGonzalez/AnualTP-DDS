package ar.edu.utn.frba.dds.scripts;

public class PokemonDTO {
    private String name;
    private String url;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        String[] segments = url.split("/");
        return Integer.parseInt(segments[segments.length - 1]);
    }

    @Override
    public String toString() {
        return "PokemonResult{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
