package portforward;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Starts forwarding between client/server. forwarding works 2 ways and is done
 * by using 2 forwardthread instacnes
 */

class ClientThread extends Thread
{
	private Socket clientSocket;
	private Socket serverSocket;
	private boolean forwardingActive = false;

	public ClientThread(Socket aClientSocket)
	{
		PortForwarder.serverHitcount++;
		clientSocket = aClientSocket;
	}

	/**
	 * create a connection with the server Forward the data between client and
	 * server
	 */
	@Override
	public void run()
	{
		InputStream clientIn;
		OutputStream clientOut;
		InputStream serverIn;
		OutputStream serverOut;
		try
		{
			// Connect to the destination server
			serverSocket = new Socket(PortForwarder.DESTINATION_HOST,
					PortForwarder.DESTINATION_PORTS[PortForwarder.getServer()]);

			// Turn on keep-alive for both the sockets
			serverSocket.setKeepAlive(true);
			clientSocket.setKeepAlive(true);

			// Obtain client & server input & output streams
			clientIn = clientSocket.getInputStream();
			clientOut = clientSocket.getOutputStream();
			serverIn = serverSocket.getInputStream();
			serverOut = serverSocket.getOutputStream();
		}
		catch (IOException ioe)
		{
			System.err
					.println("Can not connect to "
							+ PortForwarder.DESTINATION_HOST
							+ ":"
							+ PortForwarder.DESTINATION_PORTS[PortForwarder
									.getServer()]);
			brokenConnections();
			return;
		}

		// Start forwarding data between server and client
		forwardingActive = true;
		ForwardThread clientForward = new ForwardThread(this, clientIn,
				serverOut);
		clientForward.start();
		ForwardThread serverForward = new ForwardThread(this, serverIn,
				clientOut);
		serverForward.start();

		System.out.println("Forwarding "
				+ clientSocket.getInetAddress().getHostAddress() + ":"
				+ clientSocket.getPort() + " <--> "
				+ serverSocket.getInetAddress().getHostAddress() + ":"
				+ serverSocket.getPort() + " started."
				+ PortForwarder.serverHitcount);

	}

	/**
	 * close client and server sockets
	 */
	public synchronized void brokenConnections()
	{
		try
		{
			serverSocket.close();
		}
		catch (Exception e)
		{
		}
		try
		{
			clientSocket.close();
		}
		catch (Exception e)
		{
		}

		if (forwardingActive)
		{
			System.out.println("Forwarding "
					+ clientSocket.getInetAddress().getHostAddress() + ":"
					+ clientSocket.getPort() + " <--> "
					+ serverSocket.getInetAddress().getHostAddress() + ":"
					+ serverSocket.getPort() + " stopped."
					+ PortForwarder.serverHitcount);
			forwardingActive = false;
		}
	}
}
