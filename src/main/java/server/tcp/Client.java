//package server.tcp;
//
//import client.gui.ClientFrame;
//import client.gui.ServerDialog;
//import client.models.Image;
//import client.models.Shape;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.util.ArrayList;
//
//import static server.tcp.TCPProtocol.*;
//
//public class Client extends Thread {
//	public Socket s;
//	public int port;
//	public ServerDialog serverDialog;
//	public ArrayList<String> shapeNames;
//	public ClientFrame clientFrame;
//	public boolean isConnected = true;
//
//	public Client(ServerDialog serverDialog, ClientFrame clientFrame, int port) {
//		this.serverDialog = serverDialog;
//		this.clientFrame = clientFrame;
//		this.port = port;
//
//		try {
//			s = new Socket("localhost", port);
//			System.out.println("client: [started]");
//		} catch (IOException e) {
//			serverDialog.isConnected.setText("Status: " + false);
//		}
//		setDaemon(true);
//		setPriority(NORM_PRIORITY);
//	}
//
//	@Override
//	public void run() {
//		while (isConnected) {
//			try {
//				if (!s.isClosed()) {
//					LineData lineData = listenServer(s);
//					switch (lineData.code) {
//						case SEND_LIST_SIZE -> {
//							clientFrame.drawingPanel.repaint();
//							System.out.println("client: [got list size =" + (Integer)lineData.data + " ]");
//						}
//						case SEND_SHAPE -> {
//							int id = ((LineData)lineData.data).code;
//							Shape shape = (Shape)((LineData)lineData.data).data;
//							clientFrame.shapes.add(shape);
//							clientFrame.shapes.forEach(s -> {
//								s.setComponent(clientFrame.drawingPanel);
//								if (s instanceof Image) {
//									((Image) s).loadImage();
//								}
//							});
//							clientFrame.drawingPanel.repaint();
//							System.out.println("client: [got #" + id + " shape]");
//						}
//						case SEND_ALL_SHAPES -> {
//							//clientFrame.shapes = (ArrayList<Shape>)lineData.data;
//							clientFrame.shapes.forEach(s -> {
//								s.setComponent(clientFrame.drawingPanel);
//								if (s instanceof Image) {
//									((Image) s).loadImage();
//								}
//							});
//							clientFrame.drawingPanel.repaint();
//							System.out.println("client: [got all shapes]");
//						}
//						case GET_SHAPE_NAMES -> {
//							System.out.println("server: [all shape names were sent]");
//						}
//					}
//				} else break;
//			} catch (Exception e) {
//				isConnected = false;
//			}
//			System.out.println("client: [stop running]");
//		}
//	}
//}
