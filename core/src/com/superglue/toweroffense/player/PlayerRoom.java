package com.superglue.toweroffense.player;

/** Code for Population allocation and rooms, right now this is going to be implemented as functions called on the tower
 * when a region of the screen is pressed either to add or remove Population and apply effects*/
public class PlayerRoom {
    private int population;
    private String name;

    public PlayerRoom(int people, String name) {
        this.name = name;
        this.population = people;
    }

    public String getName() {
        return name;
    }
    public int getPopulation() { return population; }

    public void addPopluation(int amount) {
        population += amount;
    }
}