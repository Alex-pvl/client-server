package server.udp;

import client.gui.ClientFrame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends Thread {
	public DatagramSocket datagramSocket;
	public int portIn, portOut;
	public boolean isConnected = true;
	public ClientFrame clientFrame;

	public Server(ClientFrame clientFrame, int portOut, int portIn) {
		this.clientFrame = clientFrame;
		this.portIn = portIn;
		this.portOut = portOut;

		try {
			datagramSocket = new DatagramSocket();
			System.out.println("server: [started on port #" + portOut + "]");
		} catch (IOException e) {
			e.printStackTrace();
		}

		setDaemon(true);
		setPriority(NORM_PRIORITY);
	}

	@Override
	public void run() {
		while (isConnected) {
			try {
				Thread.sleep(100);
				DatagramPacket datagramPacket;

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = null;
				try {
					out = new ObjectOutputStream(bos);
					out.writeObject(clientFrame.shapes);

					byte[] blob = bos.toByteArray();

					datagramPacket = new DatagramPacket(blob, blob.length, InetAddress.getLocalHost(), portOut);
					datagramSocket.send(datagramPacket);
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				isConnected = false;
			}
		}
		System.out.println("server: [stopped]");
	}
}


//public class ServerUDP extends Thread {
//	private DatagramSocket socket;
//	private ArrayList<Object> shapes = new ArrayList<>();
//	private boolean isRunning = true;
//
//	public ServerUDP(int port) {
//		try {
//			socket = new DatagramSocket(port);
//			System.out.println("Server: [started on port #" + port + "]");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void run() {
//		while (isRunning) {
//			try {
//				byte[] buffer = new byte[1024];
//				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//				socket.receive(packet);
//
//				ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
//				ObjectInputStream ois = new ObjectInputStream(bais);
//				String command = (String) ois.readObject();
//
//				switch (command) {
//					case "CLEAR":
//						shapes.clear();
//						break;
//					case "ADD":
//						Object shape = ois.readObject();
//						shapes.add(shape);
//						break;
//					case "GET":
//						int index = ois.readInt();
//						if (index >= 0 && index < shapes.size()) {
//							Object result = shapes.get(index);
//							sendObject(result, packet.getAddress(), packet.getPort());
//						}
//						break;
//					case "SIZE":
//						sendObject(shapes.size(), packet.getAddress(), packet.getPort());
//						break;
//					case "GET_NAMES":
//						ArrayList<String> names = new ArrayList<>();
//						for (Object obj : shapes) {
//							names.add(obj.getClass().getSimpleName());
//						}
//						sendObject(names, packet.getAddress(), packet.getPort());
//						break;
//					default:
//						break;
//				}
//
//				ois.close();
//				bais.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		socket.close();
//		System.out.println("Server: [stopped]");
//	}
//
//	public void stopServer() {
//		isRunning = false;
//	}
//
//	private void sendObject(Object object, InetAddress address, int port) {
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(object);
//			byte[] buffer = baos.toByteArray();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
//			socket.send(packet);
//			oos.close();
//			baos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}

