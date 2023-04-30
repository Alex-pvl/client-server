package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartServer {
	public static int id = 1;
	public static final int PORT = 5000;
	public static List<Integer> clientIds = new ArrayList<>();
	public static Map<Integer, Server> clients = new HashMap<>();
	public static Map<Integer, List<String>> clientsNames = new HashMap<>();
	public static final int closeConnectionCommand = 1;
	public static final int clearCommand = 2;
	public static final int sendShapeCommand = 3;
	public static final int getShapeCommand = 4;
	public static final int sendShapeNamesCommand = 5;
	public static final int sendShapeListSizeCommand = 6;
	public static final int getShapeByIdCommand = 7;
	public static final int getShapeListSizeCommand = 8;
	public static final int getAllShapesListCommand = 9;

	public static void main(String[] args) {
		try {
			System.out.println("Сервер запущен");
			ServerSocket ss = new ServerSocket(PORT);
			while (true) {
				Socket s = ss.accept();
				Server server = new Server(s, id);
				clientIds.add(id);
				clients.put(id, server);
				id++;
				server.start();
				update();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void update() {
		for (int i = 0; i < clients.size() - 1; i++) {
			clients.get(clientIds.get(i)).update();
		}
	}

	public static void updateAll() {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(clientIds.get(i)).update();
		}
	}
}
