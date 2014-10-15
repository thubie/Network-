package p2pchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Multicast implements Runnable {
	private Thread listener;
	private MulticastSocket socket;
	private DatagramPacket outgoingData, incomingData;

	private InetAddress group;
	private int port;
	P2PChatForm form;

	public Multicast(P2PChatForm form,InetAddress group, int port) throws IOException {
		this.group = group;
		this.port = port;		
		this.form = form;
	}

	/**
	 * create a new multicast network using DataGram packets
	 * 
	 * @throws IOException
	 */
	protected void initMulticastNetwork() throws IOException {
		socket = new MulticastSocket(port);
		socket.joinGroup(group);

		outgoingData = new DatagramPacket(new byte[1], 1, group, port);
		incomingData = new DatagramPacket(new byte[65508], 65508);
	}

	/**
	 * start listening for data
	 * 
	 * @throws IOException
	 */
	public synchronized void start() throws IOException {
		if (listener == null) {
			initMulticastNetwork();
			listener = new Thread(this);
			listener.start();
			
			//show the window
			//mainWindow.setWindowVisible(true);
		}
	}

	/**
	 * create a listener thread that listens for incoming messages
	 */
	public void run() {
		try {
			while (!Thread.interrupted()) {
				incomingData.setLength(incomingData.getData().length);
				socket.receive(incomingData);
				String message = new String(incomingData.getData(), 0,
						incomingData.getLength(), "UTF8");
				form.AddNewMessage(message + "\n");
			}
		} catch (IOException ex) {
			handleIOException(ex);
		}
	}

	/**
	 * stop the program and close connections
	 * 
	 * @throws IOException
	 */
	public synchronized void stop() throws IOException {
		//mainWindow.setWindowVisible(false);
		if (listener != null) {
			listener.interrupt();
			listener = null;
			try {
				socket.leaveGroup(group);
			} finally {
				socket.close();
			}
		}
	}

	/**
	 * set the data of the outgoing DatagramPacket packet
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void setData(byte[] data) throws IOException {
		String dataString = new String(data);
		String message = form.GetUserName() + ": " + dataString;
		
		byte[] bytes = message.getBytes("UTF8");
		outgoingData.setData(bytes);
		outgoingData.setLength(bytes.length);
		socket.send(outgoingData);
	}

	/**
	 * In case something goes wrong, close the connections
	 * 
	 * @param ex
	 */
	protected synchronized void handleIOException(IOException ex) {
		if (listener != null) {
			form.AddNewMessage(ex + "\n");
			form.DisableInput();
			if (listener != Thread.currentThread())
				listener.interrupt();
			listener = null;
			try {
				socket.leaveGroup(group);
			} catch (IOException ignored) {
			}
			socket.close();
		}
	}

}
