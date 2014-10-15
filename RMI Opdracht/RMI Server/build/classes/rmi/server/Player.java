package rmi.server;

import java.rmi.RemoteException;
import rmi.player.PlayerInterface;

public class Player {
    private String clientName;
    private PlayerInterface player;
    private short score;
    private short x,y;
    
    public Player(PlayerInterface player, String clientName) {
        this.player = player;
        this.clientName = clientName;
    }
    
    public String GetName() {
        return this.clientName;
    }
    
    public PlayerInterface GetPlayer() {
        return player;
    }
    
    public short GetScore() {
        return score;
    }
    
    public void SetCoords(short x, short y) {
            this.x = x;
            this.y = y;
    }

    public void IncScore() {
            this.score++;
    }

    public short GetX() {
            return x;
    }

    public short GetY() {
            return y;
    }

    public String CoordsToString() {
            return Integer.toString(x) + "," + Integer.toString(y);
    }
    
    public void ChangePosition(Directions dir) throws RemoteException {
        //check if client is not crossing the edge
        boolean canLeft = this.x > 0;
        boolean canRight = this.x < Game.GRIDSIZE - 1;
        boolean canUp = this.y > 0;
        boolean canDown = this.y < Game.GRIDSIZE - 1;
        //move player
        switch (dir) {
            case LEFT:
                    if (canLeft)
                            x--;
                    break;
            case RIGHT:
                    if (canRight)
                            x++;
                    break;
            case UP:
                    if (canUp) 
                            y--;
                    break;
            case DOWN:
                    if (canDown)
                            y++;
                    break;
        }
    }   
    
    public boolean Collides(short x, short y) throws RemoteException {
        if((this.x == x) && (this.y == y))
            return true;
        else 
            return false;
    }
    
    public void Respawn() throws RemoteException {
        //generate random x and y between grid size
        //Math.random() generate a float between 0.0 to 1.0
        short newX = (short) (Math.random() * Game.GRIDSIZE);
        short newY = (short) (Math.random() * Game.GRIDSIZE);
        // set a new position for the player
        this.x = newX;
        this.y = newY;
    }
    
}
