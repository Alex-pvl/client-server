package server;

import client.models.Shape;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPProtocol {
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

	public static void closeConnection(Socket s) throws IOException {
		s.getOutputStream().write(CLOSE_CONNECTION);
		s.getOutputStream().flush();
		s.close();
		System.out.println("[close connection]");
	}

	public static void clearList(Socket s) throws IOException {
		s.getOutputStream().write(CLEAR_LIST);
		s.getOutputStream().flush();
		System.out.println("[clear list]");
	}

	public static void getListSize(Socket s) throws IOException {
		s.getOutputStream().write(GET_LIST_SIZE);
		s.getOutputStream().flush();
		System.out.println("[get list size]");
	}

	public static void getShape(Socket s, int id) throws IOException {
		s.getOutputStream().write(GET_SHAPE);
		s.getOutputStream().write(id);
		s.getOutputStream().flush();
		System.out.println("[get #" + id + " shape]");
	}

	public static void getAllShapes(Socket s) throws IOException {
		s.getOutputStream().write(GET_ALL_SHAPES);
		s.getOutputStream().flush();
		System.out.println("[get all shapes]");
	}

	public static void getAllShapeNames(Socket s) throws IOException {
		s.getOutputStream().write(GET_SHAPE_NAMES);
		s.getOutputStream().flush();
		System.out.println("[get all shape names]");
	}

	public static void sendListSize(Socket s, int size) throws IOException {
		s.getOutputStream().write(SEND_LIST_SIZE);
		s.getOutputStream().write(size);
		s.getOutputStream().flush();
		System.out.println("[send list size]");
	}

	public static void sendShape(Socket s, Shape shape, int id) throws IOException {
		OutputStream out = s.getOutputStream();
		out.write(SEND_SHAPE);
		out.write(id);
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(shape);
		out.flush();
		System.out.println("[send shape #" + id + "]");
	}

	public static void sendAllShapes(Socket s, ArrayList<Shape> shapes) throws IOException {
		OutputStream out = s.getOutputStream();
		out.write(SEND_ALL_SHAPES);
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(shapes);
		out.flush();
		System.out.println("[send all shapes]");
	}

	public static void sendAllShapeNames(Socket s, ArrayList<String> names) throws IOException {
		OutputStream out = s.getOutputStream();
		out.write(SEND_SHAPE_NAMES);
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(names);
		out.flush();
		objOut.flush();
		System.out.println("[send all shape names]");
	}

	public static LineData listenServer(Socket s) throws IOException, ClassNotFoundException {
		InputStream in = s.getInputStream();
		System.out.println("[client input stream size = " + in.available() + "]");
		int code = in.read();
		LineData lineData = new LineData();
		lineData.code = code;
		switch (code) {
			case SEND_LIST_SIZE -> lineData.data = in.read();
			case SEND_SHAPE -> {
				LineData shape = new LineData();
				shape.code = in.read();
				ObjectInputStream objIn = new ObjectInputStream(in);
				shape.data = (Shape) objIn.readObject();
				lineData.data = shape;
			}
			case SEND_ALL_SHAPES -> {
				ObjectInputStream objIn = new ObjectInputStream(in);
				lineData.data = (ArrayList<Shape>) objIn.readObject();
			}
			case GET_SHAPE_NAMES -> {
				ObjectInputStream objIn = new ObjectInputStream(in);
				lineData.data = (ArrayList<String>) objIn.readObject();
			}
			default -> {
				lineData.code = UNKNOWN;
				lineData.data = null;
			}
		}
		return lineData;
	}

	public static LineData listenClient(Socket s) throws IOException, ClassNotFoundException {
		InputStream in = s.getInputStream();
		int code = in.read();
		LineData lineData = new LineData();
		lineData.code = code;

		switch (code) {
			case GET_LIST_SIZE, GET_SHAPE -> lineData.data = in.read();
			case CLOSE_CONNECTION, CLEAR_LIST, GET_ALL_SHAPES -> {}
			case SEND_SHAPE_NAMES -> {
				ObjectInputStream objIn = new ObjectInputStream(in);
				lineData.data = (ArrayList<String>) objIn.readObject();
			}
			default -> {
				lineData.code = UNKNOWN;
				lineData.data = null;
			}
		}
		return lineData;
	}
}
