package game;

public enum Figure {
    ROCK("камень"),
    SCISSORS("ножницы"),
    PAPER("бумага");

    private final String name;

    Figure(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
