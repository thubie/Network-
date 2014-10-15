package portforward;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles forwards between input and output streams. When the stream fails the
 * sockets will close
 */
class ForwardThread extends Thread
{
	private static final int BUFFER_SIZE = 8192;

	InputStream inputStream;
	OutputStream outputStream;
	ClientThread parent;

	/**
	 * Creates a new traffic redirection thread with its parent, input
	 * stream and output stream.
	 */
	public ForwardThread(ClientThread aParent, InputStream aInputStream,
			OutputStream aOutputStream)
	{
		parent = aParent;
		inputStream = aInputStream;
		outputStream = aOutputStream;
	}

	/**
	 * Runs the thread. reads from the input stream and writes to the output stream.
	 * exits the thread and warns the parent upon failure
	 */
	@Override
	public void run()
	{
		byte[] buffer = new byte[BUFFER_SIZE];
		try
		{
			while (true)
			{
				int bytesRead = inputStream.read(buffer);
				if (bytesRead == -1)
					break; // End of stream is reached --> exit
				outputStream.write(buffer, 0, bytesRead);
				outputStream.flush();
			}
		}
		catch (IOException e)
		{
			// Read/write failed --> connection is broken
		}

		// Notify parent thread that the connection is broken
		parent.brokenConnections();
	}
}