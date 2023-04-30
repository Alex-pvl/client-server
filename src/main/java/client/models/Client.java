package client.models;

import client.gui.ServerDialog;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
	public Socket socket = null;
	public int id;
	public int idToSwap;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public int command;
	public ServerDialog serverDialog;
	public boolean isConnected = true;

	public Client(ServerDialog serverDialog) {
		this.serverDialog = serverDialog;
	}

	@Override
	public void run() {
		try {
			socket = new Socket("127.0.0.1", 5000);
			isConnected = true;
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			List<Integer> clientIds = (ArrayList<Integer>) in.readObject();
			id = in.readInt();
			serverDialog.getListOfClients(clientIds, id);

			while (isConnected) {
				if (in.available() == 0) {
					if (command != -1) {
						switch (command) {
							case 1 -> {}
							case 2 -> {}
							case 3 -> {}
							case 4 -> {}
							case 5 -> {}
							case 6 -> {}
							case 7 -> {}
							case 8 -> {}
							case 9 -> {}
							default -> {}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
