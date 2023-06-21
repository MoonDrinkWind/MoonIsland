package icu.moondrinkwind.moonisland.entity;

import org.bukkit.entity.Player;

import java.util.List;

public class Island {
    private String ID;
    private String name;
    private int startX;
    private int startZ;
    private int endX;
    private int endZ;
    private Player owner;
    private List<Player> members;
    private int centerX;
    private int centerZ;

    public String getName() {
        return name;
    }

    public Island setName(String name) {
        this.name = name;
        return this;
    }

    public int getStartX() {
        return startX;
    }

    public Island setStartX(int startX) {
        this.startX = startX;
        return this;
    }

    public int getStartZ() {
        return startZ;
    }

    public Island setStartZ(int startZ) {
        this.startZ = startZ;
        return this;
    }

    public int getEndX() {
        return endX;
    }

    public Island setEndX(int endX) {
        this.endX = endX;
        return this;
    }

    public int getEndZ() {
        return endZ;
    }

    public Island setEndZ(int endZ) {
        this.endZ = endZ;
        return this;
    }

    public Player getOwner() {
        return owner;
    }

    public Island setOwner(Player owner) {
        this.owner = owner;
        return this;
    }

    public List<Player> getMembers() {
        return members;
    }

    public Island setMembers(List<Player> members) {
        this.members = members;
        return this;
    }

    public int getCenterX() {
        return centerX;
    }

    public Island setCenterX(int centerX) {
        this.centerX = centerX;
        return this;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public Island setCenterZ(int centerZ) {
        this.centerZ = centerZ;
        return this;
    }

    public String getID() {
        return ID;
    }

    public Island setID(String ID) {
        this.ID = ID;
        return this;
    }
}
