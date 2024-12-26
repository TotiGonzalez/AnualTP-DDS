package ar.edu.utn.frba.dds.scripts;
import java.util.List;

public class PokedexList {
    private int count;
    private String next;
    private String previous;
    private List<PokemonDTO> results;

    // Getters and Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokemonDTO> getResults() {
        return results;
    }

    public void setResults(List<PokemonDTO> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PokemonDTO{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
