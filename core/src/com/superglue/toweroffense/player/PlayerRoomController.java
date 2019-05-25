package com.superglue.toweroffense.player;

import java.util.Hashtable;

public class PlayerRoomController {
    private Player player;
    // lookup rooms by their names
    private Hashtable<String, PlayerRoom> rooms;

    private PlayerRoom engineRoom = new PlayerRoom(PlayerConstants.DEFAULT_PEOPLE_PER_CLICK, "Engine Room");
    private PlayerRoom sacrificeRoom = new PlayerRoom (0, "Sacrifice Room");
    private PlayerRoom lifeStealRoom = new PlayerRoom(0, "LifeSteal Room");
    private PlayerRoom hardenRoom = new PlayerRoom(0, "Harden Room");

    public PlayerRoomController(Player player) {
        this.player = player;

        this.rooms = new Hashtable<>();
        this.rooms.put(engineRoom.getName(), engineRoom);
        this.rooms.put(sacrificeRoom.getName(), sacrificeRoom);
        this.rooms.put(lifeStealRoom.getName(), lifeStealRoom);
        this.rooms.put(hardenRoom.getName(), hardenRoom);
    }

    public PlayerRoom getRoom(String name) { return rooms.get(name); }

    public void engineRoomCheck () {
        if (engineRoom.getPopulation() < 10) {
            player.setMovementSpeedScale(0);
        } else {
            float speedBuff = .01f * engineRoom.getPopulation();
            player.setMovementSpeedScale(1 + speedBuff);
        }
    }

    public void sacrificeRoomCheck () {
        int population = sacrificeRoom.getPopulation();
        float regen = population;
        player.setSacrificed(population);
        player.setOldPopulation(player.getOldPopulation()-population);
        sacrificeRoom.addPopluation(-population);
        if(player.getHealth() + regen <= 100) {
//            player.setMaxHealth((int) (maxHealth + regen));
            player.addHealth(regen);
        }
        else {
            player.setHealth(player.getMaxHealth());
        }
    }

    public void lifeStealRoomCheck (int justCrushed) {
        float regen = (.01f * lifeStealRoom.getPopulation())*justCrushed;
        if (lifeStealRoom.getPopulation()> 100){
            regen = justCrushed;
        }

        player.addHealth(regen);
    }

    public void hardenRoomCheck(){
        int population = hardenRoom.getPopulation();

        player.setCrushPower((population + PlayerConstants.DEFAULT_PEOPLE_PER_CLICK) / 10);

        if (population < PlayerConstants.DEFAULT_PEOPLE_PER_CLICK) {player.setDefenseBuffScale(1);}
        if (population > 20 * PlayerConstants.DEFAULT_PEOPLE_PER_CLICK) {player.setDefenseBuffScale(.5f);}
        //Maxes out at a pop of 200
        else {
            player.setDefenseBuffScale(1 - (population * .0025f));
        }
    }

    public void addToRoom(PlayerRoom room, int amount){
        if (player.getPopulation() >= amount) {
            player.addPopulation(-amount);
            room.addPopluation(amount);
        }
    }
    public void removeFromRoom(PlayerRoom room, int amount){
        if (room.getPopulation()> 0) {
            player.addPopulation(amount);
            room.addPopluation(-amount);
        }
    }
}
