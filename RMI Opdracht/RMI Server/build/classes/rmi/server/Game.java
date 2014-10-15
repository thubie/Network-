package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import rmi.player.PlayerInterface;

public class Game extends UnicastRemoteObject implements ServerInterface {
    private final ServerForm form;
    private ArrayList<Player> players = new ArrayList<Player>();
    public static final short GRIDSIZE = 15;
    
    public Game(ServerForm form) throws RemoteException {
        this.form = form;
        form.ServerActive();
    }
    
    @Override
    public void RegisterPlayer(PlayerInterface remoteplayer, String playerName) throws RemoteException {
        Player player = new Player(remoteplayer, playerName);
        players.add(player);
        //spawn player in grid and 
        player.Respawn();
        player.GetPlayer().SetPosition(player.GetX(), player.GetY());
    }

    @Override
    public void MovePlayer(PlayerInterface remotePlayer, Directions direction) throws RemoteException {    
        Player player = GetPlayer(remotePlayer);
        player.ChangePosition(direction);
        remotePlayer.SetPosition(player.GetX(), player.GetY());
        if(Colliding(player)) {
            player.IncScore();
            UpdateScores();
        }
    }

    @Override
    public void LogoutPlayer(PlayerInterface remoteplayer, String playerName) throws RemoteException {
        //removes players from playerlist
        players.remove(GetPlayer(remoteplayer));
    }
    
    public boolean Colliding(Player player) throws RemoteException {
        for (int i = 0; i < players.size(); i++) {
            Player otherPlayer = players.get(i);
            if (!player.GetPlayer().equals(otherPlayer.GetPlayer())) 
            {
                if (otherPlayer.Collides(player.GetX(), player.GetY())) 
                {
                        Respawn(otherPlayer);
                        return true;
                }
            }
        }
        return false;
    }
    
    private Player GetPlayer(PlayerInterface remotePlayer) {
        for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (player.GetPlayer().equals(remotePlayer)) {
                        return player;
                }
        }
        return null;
    }
    
    public void Respawn(Player player) throws RemoteException {
        // get new coords
        player.Respawn();
        try {
            player.GetPlayer().SetPosition(player.GetX(), player.GetY());
        } catch (RemoteException e) {
            LogoutPlayer(player.GetPlayer(), player.GetName());
        }
        for (int i = 0; i < players.size(); i++) {
            Player otherPlayer = players.get(i);
            // dont compare if it's the same player
            if (!player.GetPlayer().equals(otherPlayer.GetPlayer())) {
                if ((player.GetX() == otherPlayer.GetX()) && player.GetY() == otherPlayer.GetY()) {
                    // recursive call
                    Respawn(player);
                }
            }
        }
        System.out.println(player.GetName() + " has spawned at "
                        + player.GetX() + "," + player.GetY());
    }

    //look into this 
    private void UpdateScores() throws RemoteException {
        //iterate through every active player and update their score
        String score;
        for (int i = 0; i < players.size(); i++) {
           Player player = players.get(i);
           PlayerInterface remoteplayer = player.GetPlayer();
           score = Integer.toString(player.GetScore());
           try {
               remoteplayer.SetScores(score);
           }catch(RemoteException e) {
               LogoutPlayer(remoteplayer, players.get(i).GetName() );
           }                 
        }
    }
    
}
