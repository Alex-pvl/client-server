package server.udp;

import client.models.Shape;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UDPProtocol {
	public static final int UNKNOWN = -1;
	public static final int CLOSE_CONNECTION = 0;
	public static final int CLEAR_LIST = 1;
	public static final int GET_LIST_SIZE = 2;
	public static final int GET_SHAPE = 3;
	public static final int GET_ALL_SHAPES = 4;
	public static final int GET_SHAPE_NAMES = 5;

	public static final int SEND_LIST_SIZE = 6;
	public static final int SEND_SHAPE = 7;
	public static final int SEND_ALL_SHAPES = 8;
	public static final int SEND_SHAPE_NAMES = 9;

	public static class LineData {
		public int code;
		public Object data;
	}

	public static void closeConnection(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] data = new byte[] {CLOSE_CONNECTION};
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[close connection]");
	}

	public static void clearList(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] data = new byte[] {CLEAR_LIST};
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[clear list]");
	}

	public static void getListSize(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] data = new byte[] {GET_LIST_SIZE};
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[get list size]");
	}

	public static void getShape(DatagramSocket socket, InetAddress address, int port, int id) throws IOException {
		byte[] data = new byte[] {GET_SHAPE, (byte)id};
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[get #" + id + " shape]");
	}

	public static void getAllShapes(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] data = new byte[] {GET_ALL_SHAPES};
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[get all shapes]");
	}

	public static void getAllShapeNames(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] data = new byte[] {GET_SHAPE_NAMES};
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[get all shape names]");
	}

	public static void sendListSize(DatagramSocket socket, InetAddress address, int port, int size) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeByte(SEND_LIST_SIZE);
		dos.writeInt(size);
		dos.flush();
		byte[] data = bos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[send list size]");
	}

	public static void sendShape(DatagramSocket socket, InetAddress address, int port, Shape shape, int id) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeByte(SEND_SHAPE);
		dos.writeInt(id);
		ObjectOutputStream objOut = new ObjectOutputStream(dos);
		objOut.writeObject(shape);
		objOut.flush();
		byte[] data = bos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[send shape #" + id + "]");
	}

	public static void sendAllShapes(DatagramSocket socket, InetAddress address, int port, ArrayList<Shape> shapes) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeByte(SEND_ALL_SHAPES);
		ObjectOutputStream objOut = new ObjectOutputStream(dos);
		objOut.writeObject(shapes);
		objOut.flush();
		byte[] data = bos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[send all shapes]");
	}

	public static void sendAllShapeNames(DatagramSocket socket, InetAddress address, int port, ArrayList<String> names) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeByte(SEND_SHAPE_NAMES);
		ObjectOutputStream objOut = new ObjectOutputStream(dos);
		objOut.writeObject(names);
		objOut.flush();
		byte[] data = bos.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
		System.out.println("[send all shape names]");
	}

	public static LineData listenServer(DatagramSocket socket) throws IOException, ClassNotFoundException {
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		LineData lineData = new LineData();
		lineData.code = packet.getData()[0];

		ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData(), 1, packet.getLength() - 1);
		DataInputStream dis = new DataInputStream(bis);

		switch (lineData.code) {
			case SEND_LIST_SIZE -> lineData.data = dis.readInt();
			case SEND_SHAPE -> {
				LineData shape = new LineData();
				shape.code = dis.readInt();
				ObjectInputStream objIn = new ObjectInputStream(dis);
				shape.data = (Shape) objIn.readObject();
				lineData.data = shape;
			}
			case SEND_ALL_SHAPES -> {
				ObjectInputStream objIn = new ObjectInputStream(dis);
				lineData.data = (ArrayList<Shape>) objIn.readObject();
			}
			case GET_SHAPE_NAMES -> {
				ObjectInputStream objIn = new ObjectInputStream(dis);
				lineData.data = (ArrayList<String>) objIn.readObject();
			}
			default -> {
				lineData.code = UNKNOWN;
				lineData.data = null;
			}
		}
		return lineData;
	}

	public static LineData listenClient(DatagramSocket socket) throws IOException, ClassNotFoundException {
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);

		LineData lineData = new LineData();
		lineData.code = buffer[0];

		switch (lineData.code) {
			case GET_LIST_SIZE:
			case GET_SHAPE:
				lineData.data = buffer[1];
				break;
			case CLOSE_CONNECTION:
			case CLEAR_LIST:
			case GET_ALL_SHAPES:
				break;
			case SEND_SHAPE_NAMES:
				ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(buffer, 1, packet.getLength() - 1));
				lineData.data = (ArrayList<String>) objIn.readObject();
				break;
			default:
				lineData.code = UNKNOWN;
				lineData.data = null;
				break;
		}

		return lineData;
	}
}
