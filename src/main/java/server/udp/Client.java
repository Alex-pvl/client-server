//package server.udp;
//
//import client.gui.ClientFrame;
//import client.models.Image;
//import client.models.Shape;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.ArrayList;
//
//public class Client extends Thread {
//	public DatagramSocket datagramSocket;
//	public ClientFrame clientFrame;
//	public int portIn, portOut;
//	public boolean isConnected = true;
//
//	public Client(ClientFrame clientFrame, int portOut, int portIn) {
//		this.clientFrame = clientFrame;
//		this.portIn = portIn;
//		this.portOut = portOut;
//
//		try {
//			datagramSocket = new DatagramSocket(portIn, InetAddress.getLocalHost());
//			System.out.println("client: [started on port #" + portIn + "]");
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
//				String messageFromServer = new String(buffer, 0, packet.getLength());
//				handleMessage(messageFromServer);
//			} catch (IOException e) {
//
//			}
//		}
//	}
//
//	public void send(String message) {
//		try {
//			byte[] buffer = message.getBytes();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), portOut);
//			datagramSocket.send(packet);
//			System.out.println("client: [sent \"" + message + "\" command]");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void handleMessage(String message) {
//		String[] parts = message.trim().split(":");
//		String command = parts[0];
//		switch (command) {
//			case "CLOSE" -> {
//				clientFrame.connectionStatusLabel.setText("Connection: " + false);
//				datagramSocket.close();
//				System.out.println("client: [connection closed]");
//			}
//			case "SEND SIZE" -> {
//				int size = Integer.parseInt(parts[1]);
//				clientFrame.shapesSizeLabel.setText("Size: " + size);
//				System.out.println("client: [received list size = " + size + "]");
//			}
//			case "SEND SHAPE" -> {
//				Shape shape = receiveShape();
//				if (shape instanceof Image) {
//					((Image) shape).loadImage();
//				}
//				shape.setComponent(clientFrame.drawingPanel);
//				clientFrame.shapes.add(shape);
//				clientFrame.drawingPanel.repaint();
//			}
//			case "SEND ALL SHAPES" -> {
//				clientFrame.shapes = receiveAllShapes();
//				clientFrame.shapes.forEach(s -> {
//					if (s instanceof Image) {
//						((Image) s).loadImage();
//					}
//					s.setComponent(clientFrame.drawingPanel);
//				});
//				clientFrame.drawingPanel.repaint();
//			}
//			default -> System.out.println("client: [invalid command]");
//		}
//	}
//
//	private Shape receiveShape() {
//		try {
//			byte[] buffer = new byte[1024];
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//			datagramSocket.receive(packet);
//
//			ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
//			ObjectInputStream ois = new ObjectInputStream(bais);
//			Object object = ois.readObject();
//
//			if (object instanceof Shape) {
//				return (Shape) object;
//			}
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private ArrayList<Shape> receiveAllShapes() {
//		try {
//			byte[] buffer = new byte[1024];
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//			datagramSocket.receive(packet);
//
//			ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
//			ObjectInputStream ois = new ObjectInputStream(bais);
//			Object object = ois.readObject();
//
//			return (ArrayList<Shape>) object;
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//}