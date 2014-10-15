package rmi.player;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements PlayerInterface {
    PlayerForm form;
    
    public Player(PlayerForm form) throws RemoteException{
        this.form = form;
    }
    
    
    @Override
    public void SetScores(String scores) throws RemoteException {
        form.DisplayScore(scores);
    }
    
    
    @Override
    public void SetPosition(short x, short y) throws RemoteException {
        form.DrawPosition(x, y);
    }
    
}
