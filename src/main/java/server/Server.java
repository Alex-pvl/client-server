package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	public Socket s;
	public int id;
	public int command;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public boolean isConnected = true;

	public Server(Socket s, int id) {
		this.s = s;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			out.writeObject(StartServer.clientIds);
			out.writeInt(id);
			out.flush();

			while (isConnected) {
				command = in.readInt();
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
					default -> System.out.println("Invalid command");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnect();
			StartServer.updateAll();
		}
	}

	public void update() {
		try {
			out.writeInt(9);
			List<Integer> newClientIds = new ArrayList<>(StartServer.clientIds);
			out.writeObject(newClientIds);
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
			StartServer.clients.remove(id);
			for (int i = 0; i < StartServer.clientIds.size(); i++) {
				if (StartServer.clientIds.get(i) == id) {
					StartServer.clientIds.remove(i);
					i--;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
