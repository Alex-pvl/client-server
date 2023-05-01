package server;

import client.gui.ClientFrame;
import client.gui.ServerDialog;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {
	public Socket s = null;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public int command;
	public int port;
	public ServerDialog serverDialog;
	public ClientFrame clientFrame;
	public boolean isConnected = true;

	public Client(ServerDialog serverDialog, int port) {
		this.serverDialog = serverDialog;
		this.clientFrame = (ClientFrame)serverDialog.getParent();
		this.port = port;

		try {
			s = new Socket("localhost", port);
			System.out.println("Client started");
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDaemon(true);
		setPriority(NORM_PRIORITY);
	}

	@Override
	public void run() {
		try {
			s = new Socket("localhost", port);
			isConnected = true;
			this.serverDialog.isConnected.setText("Status: " + true);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());

			while (isConnected) {
				if (in.available() == 0) {
					if (command != -1) {
						switch (command) {

						}
					}
				} else {
					int commandFromServer = in.readInt();
					System.out.println("Command id from server: " + commandFromServer);
					switch (commandFromServer) {

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Closing...");
			try {
				in.close();
				out.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {
		this.command = 1;
		this.serverDialog.isConnected.setText("Status: " + false);
	}

	public void clear() {
		this.command = 2;
		clientFrame.shapes.clear();
	}

	// TODO остальные команды

}
