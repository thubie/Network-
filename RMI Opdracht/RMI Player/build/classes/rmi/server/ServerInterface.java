package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import rmi.player.PlayerInterface;

public interface ServerInterface extends Remote{
    public void RegisterPlayer(PlayerInterface remoteplayer, String playerName) throws RemoteException;
    public void MovePlayer(PlayerInterface remoteplayer, Directions direction) throws RemoteException;
    public void LogoutPlayer(PlayerInterface remoteplayer, String playerName) throws RemoteException;
}
