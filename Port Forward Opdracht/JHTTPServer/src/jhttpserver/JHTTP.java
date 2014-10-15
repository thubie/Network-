package jhttpserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class JHTTP extends Thread {
	private File documentRootDirectory;
	private String indexFileName = "index.html";
	private ServerSocket server;
	private int numThreads = 50;
	
	private static Frame frame;

	public JHTTP(File documentRootDirectory, int port, String indexFileName)
			throws IOException {
		if (!documentRootDirectory.isDirectory()) {
			throw new IOException(documentRootDirectory
					+ " does not exist as a directory");
		}
		this.documentRootDirectory = documentRootDirectory;
		if (port == 80 || port == 8888 || port == 9999)
		{
			this.indexFileName = indexFileName;
		}
		else
		{
			this.indexFileName = indexFileName;
		}
		this.server = new ServerSocket(port);
	}

	public JHTTP(File documentRootDirectory, int port) throws IOException {
		this(documentRootDirectory, port, "index.html");
	}

	public JHTTP(File documentRootDirectory) throws IOException {
		this(documentRootDirectory, 80, "index.html");
	}

	public void run() {
		for (int i = 0; i < numThreads; i++) {
			Thread t = new Thread(new RequestProcessor(documentRootDirectory,
					indexFileName));
			t.start();
		}
		System.out.println("Accepting connections on port "
				+ server.getLocalPort());
		frame.writeLabel("Accepting connections on port "
				+ server.getLocalPort());
		System.out.println("Document Root: " + documentRootDirectory);
		while (true) {
			try {
				Socket request = server.accept();
				RequestProcessor.processRequest(request);
			} catch (IOException ex) {
			}
		}
	}

	public static void main(String[] args) {
		// get the Document root
		File docroot;
		try {
			String path = System.getProperty("user.dir");
//			docroot = new File(args[0]);
			docroot = new File(path);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Usage: java JHTTP docroot port indexfile");
			return;
		}
		// set the port to listen on
		int port;
		try {
			port = 9999; //assign port
			if (port < 0 || port > 65535)
				port = 80;
		} catch (Exception ex) {
			port = 80;
		}
		try {
			frame = new Frame();
			JHTTP webserver = new JHTTP(docroot, port);
			webserver.start();
		} catch (IOException ex) {
			System.out.println("Server could not start because of an "
					+ ex.getClass());
			System.out.println(ex);
		}
	}
}