//package server.udp;
//
//import client.gui.ClientFrame;
//import client.models.Shape;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.ArrayList;
//
//public class Server extends Thread {
//	public DatagramSocket datagramSocket;
//	public int portIn, portOut;
//	public boolean isConnected = true;
//	public ClientFrame clientFrame;
//
//	public Server(ClientFrame clientFrame, int portOut, int portIn) {
//		this.clientFrame = clientFrame;
//		this.portIn = portIn;
//		this.portOut = portOut;
//
//		try {
//			datagramSocket = new DatagramSocket(portOut);
//			clientFrame.connectionStatusLabel.setText("Connection: " + true);
//			System.out.println("server: [started on port #" + portOut + "]");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		setDaemon(true);
//		setPriority(NORM_PRIORITY);
//	}
//
//	@Override
//	public void run() {
//		while (isConnected) {
//			byte[] buffer = new byte[1024];
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//			try {
//				datagramSocket.receive(packet);
//				String messageFromClient = new String(buffer, 0, packet.getLength());
//				handleMessage(messageFromClient);
//			} catch (IOException e) {
//
//			}
//		}
//		datagramSocket.close();
//		System.out.println("server: [stopped]");
//	}
//
//	public void send(String message) {
//		try {
//			byte[] buffer = message.getBytes();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), portIn);
//			datagramSocket.send(packet);
//			System.out.println("server: [sent \"" + message + "\" command]");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void handleMessage(String message) {
//		String[] parts = message.trim().split(":");
//		String command = parts[0];
//		switch (command) {
//			case "CLEAR" -> {
//				clientFrame.shapes.clear();
//				clientFrame.drawingPanel.repaint();
//				System.out.println("server: [shapes list cleared]");
//			}
//			case "CLOSE" -> {
//				clientFrame.connectionStatusLabel.setText("Connection: " + false);
//				send("CLOSE");
//				datagramSocket.close();
//				System.out.println("server: [connection closed]");
//			}
//			case "GET SIZE" -> {
//				int size = clientFrame.shapes.size();
//				send("SEND SIZE:" + size);
//			}
//			case "SEND NAMES" -> {
//				var names = parts[1];
//				System.out.println("server: [received shape names: " + names);
//			}
//			case "GET SHAPE" -> {
//				int id = Integer.parseInt(parts[1]);
//				Shape s = clientFrame.shapes.get(id);
//				send("SEND SHAPE");
//				sendShape(s);
//			}
//			case "GET ALL SHAPES" -> {
//				var shapes = clientFrame.shapes;
//				send("SEND ALL SHAPES");
//				sendAllShapes(shapes);
//			}
//			default -> System.out.println("server: [invalid command]");
//		}
//	}
//
//	private void sendShape(Shape shape) {
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(shape);
//			oos.flush();
//			byte[] buffer = baos.toByteArray();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), portIn);
//			datagramSocket.send(packet);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void sendAllShapes(ArrayList<Shape> shapes) {
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(shapes);
//			oos.flush();
//			byte[] buffer = baos.toByteArray();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), portIn);
//			datagramSocket.send(packet);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}