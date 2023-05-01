package server;

import client.models.Shape;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Protocol {
	public static final int UNKNOWN = -1;

	public static final int CLOSE_CONNECTION = 0;
	public static final int CLEAR_LIST = 1;
	public static final int GET_LIST_SIZE = 2;
	public static final int GET_OBJECT = 3;
	public static final int GET_ALL_OBJECTS = 4;

	public static final int SEND_LIST_SIZE = 5;
	public static final int SEND_OBJECT = 6;
	public static final int SEND_ALL_OBJECTS = 7;

	public static class Data {
		public int code;
		public Object data;
	}

	static void closeConnection(Socket s) {
		try {
			s.getOutputStream().write(CLOSE_CONNECTION);
			s.getOutputStream().flush();
			s.close();
			System.out.println("client: disconnected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void clearList(Socket s) {
		try {
			s.getOutputStream().write(CLEAR_LIST);
			s.getOutputStream().flush();
			System.out.println("client: clear list");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void getListSize(Socket s) {
		try {
			s.getOutputStream().write(GET_LIST_SIZE);
			s.getOutputStream().flush();
			System.out.println("client: get list size");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void getObject(Socket s, int id) {
		try {
			s.getOutputStream().write(GET_OBJECT);
			s.getOutputStream().write(id);
			s.getOutputStream().flush();
			System.out.println("client: get {" + id + "}th object");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void getAllAbjects(Socket s) {
		try {
			s.getOutputStream().write(GET_ALL_OBJECTS);
			s.getOutputStream().flush();
			System.out.println("client: get all objects");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void sendListSize(Socket s, int size) {
		try {
			s.getOutputStream().write(SEND_LIST_SIZE);
			s.getOutputStream().write(size);
			s.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void sendObject(Socket s, Shape shape, int id) {
		try {
			OutputStream out = s.getOutputStream();
			out.write(SEND_OBJECT);
			out.write(id);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(shape);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void sendAllObjects(Socket s, List<Shape> shapes) {
		try {
			OutputStream out = s.getOutputStream();
			out.write(SEND_ALL_OBJECTS);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(shapes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Data listenServer(Socket s) {
		try {
			InputStream in = s.getInputStream();
			System.out.println("client: inputStream size = " + in.available());
			int code = in.read();
			Data data = new Data();
			data.code = code;
			switch (code) {
				case SEND_LIST_SIZE -> data.data = in.read();
				case SEND_OBJECT -> {
					Data obj = new Data();
					obj.code = in.read();
					ObjectInputStream objIn = new ObjectInputStream(in);
					obj.data = (Shape) objIn.readObject();
					data.data = obj;
				}
				case SEND_ALL_OBJECTS -> {
					ObjectInputStream objIn = new ObjectInputStream(in);
					data.data = (ArrayList<Shape>) objIn.readObject();
				}
				default -> {
					data.code = UNKNOWN;
					data.data = null;
				}
			}
			return data;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	static Data listenClient(Socket s) {
		try {
			InputStream in = s.getInputStream();
			int code = in.read();
			Data data = new Data();
			data.code = code;

			switch (code) {
				case GET_LIST_SIZE -> data.data = in.read();
				case GET_OBJECT ->  data.data = in.read();
				case CLOSE_CONNECTION -> {}
				case CLEAR_LIST -> {}
				case GET_ALL_OBJECTS -> {}
				default -> {data.code = UNKNOWN; data.data = null;}
			}
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
