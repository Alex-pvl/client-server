package server;

import client.gui.ClientFrame;
import client.models.Image;
import client.models.Shape;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static server.TCPProtocol.*;

public class Server extends Thread {
	public ServerSocket ss;
	public Socket s;
	public ClientFrame clientFrame;
	int port;

	public Server(ClientFrame clientFrame, int port) {
		this.clientFrame = clientFrame;
		this.port = port;

		try {
			ss = new ServerSocket(port);
			System.out.println("server: [started on port #" + port + "]");
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
		while (true) {
			try {
				if (!s.isClosed()) {
					LineData lineData = listenClient(s);

					switch (lineData.code) {
						case CLEAR_LIST -> {
							clientFrame.shapes.clear();
							clientFrame.drawingPanel.repaint();
							System.out.println("server: [shapes list cleared]");
						}
						case GET_SHAPE -> {
							int id = (Integer)lineData.data;
							sendShape(s, clientFrame.shapes.get(id), id);
							clientFrame.drawingPanel.repaint();
							System.out.println("server: [shape #" + id + " was sent]");
						}
						case GET_ALL_SHAPES -> {
							sendAllShapes(s, clientFrame.shapes);
							clientFrame.drawingPanel.repaint();
							System.out.println("server: [all shapes were sent]");
						}
						case SEND_SHAPE_NAMES -> {
							getAllShapeNames(s);
							var shapeNames = (ArrayList<String>)lineData.data;
							shapeNames.forEach(System.out::println);
							clientFrame.drawingPanel.repaint();
							System.out.println("client: [got all shape names]");
						}
						case GET_LIST_SIZE -> {
							int size = clientFrame.shapes.size();
							sendListSize(s, size);
							clientFrame.drawingPanel.repaint();
							clientFrame.serverDialog.listSizeLabel.setText("size: " + size);
							System.out.println("server: [shapes list size was sent]");
						}
						case CLOSE_CONNECTION -> {
							closeConnection(s);
							clientFrame.drawingPanel.repaint();
							System.out.println("server: [connection closed]");
						}
					}
				} else {
					ss.close();
					break;
				}
			} catch (IOException | ClassNotFoundException e) {

			}
		}
	}
}
