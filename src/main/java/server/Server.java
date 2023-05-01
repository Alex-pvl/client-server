package server;

import client.gui.ClientFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	public ServerSocket ss;
	public Socket s;
	public int command;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public boolean isConnected = true;
	public ClientFrame clientFrame;
	int port;

	public Server(ClientFrame clientFrame, int port) {
		this.clientFrame = clientFrame;
		this.port = port;

		try {
			ss = new ServerSocket(port);
			System.out.println("Server started on port #" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setDaemon(true);
		setPriority(NORM_PRIORITY);

		try {
			s = ss.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());

			while (isConnected) {
				command = in.readInt();
				switch (command) {
					case 0 -> {
						System.out.println("Connecting");
					}
					case 1 -> {
						System.out.println("Disconnecting...");
						disconnect();
					}
					case 2 -> {
						System.out.println("Clear shapes");

					}
					case 3 -> {}
					case 4 -> {}
					case 5 -> {}
					case 6 -> {}
					case 7 -> {}
					case 8 -> {}
					default -> System.out.println("Invalid command");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	public void update() {
		try {
			out.writeInt(9);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			isConnected = false;
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
