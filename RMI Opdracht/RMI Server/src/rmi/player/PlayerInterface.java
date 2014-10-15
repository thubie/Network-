package rmi.player;

import java.rmi.Remote;
import java.rmi.RemoteException;

//Player interface methods need to throw RemoteExceptions
public interface PlayerInterface extends Remote {
    public void SetScores(String scores) throws RemoteException;
    public void SetPosition(short x, short y) throws RemoteException;
}

