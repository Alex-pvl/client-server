package server.udp;

import client.gui.ClientFrame;
import client.models.Shape;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client extends Thread {
	public DatagramSocket datagramSocket;
	public ClientFrame clientFrame;
	public int portIn, portOut;
	public boolean isConnected = true;

	public Client(ClientFrame clientFrame, int portOut, int portIn) {
		this.clientFrame = clientFrame;
		this.portIn = portIn;
		this.portOut = portOut;

		try {
			datagramSocket = new DatagramSocket(portIn, InetAddress.getLocalHost());
			System.out.println("client: [started on port # " + portIn +"]");
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
				byte[] data = new byte[4096];
				DatagramPacket datagramPacket = new DatagramPacket(data, data.length);

				datagramSocket.receive(datagramPacket);

				ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInput in;

				in = new ObjectInputStream(bis);
				clientFrame.shapes = (CopyOnWriteArrayList<Shape>) in.readObject();
				clientFrame.shapes.forEach(System.out::println);
				clientFrame.drawingPanel.repaint();

				bis.close();
				in.close();

			} catch (Exception e) {
				isConnected = false;
			}
			System.out.println("client: [stop running]");
		}
	}
//
//	public void sendCommand(String command) {
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(command);
//			byte[] buff = baos.toByteArray();
//			DatagramPacket packet = new DatagramPacket(buff, buff.length);
//			s.send(packet);
//		} catch (IOException e) {
//			System.out.println("error: sending command \"" + command + "\"");
//		}
//	}
}

//public class ClientUDP {
//	private DatagramSocket socket;
//	private InetAddress address;
//	private int port;
//
//	public ClientUDP(String hostname, int port) {
//		try {
//			socket = new DatagramSocket();
//			address = InetAddress.getByName(hostname);
//			this.port = port;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void sendCommand(String command) {
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(command);
//			byte[] buffer = baos.toByteArray();
//			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,InetAddress address = InetAddress.getByName("localhost");
//			socket.send(packet);
//		} catch (IOException e) {
//			System.err.println("Error sending command: " + e.getMessage());
//		}
//	}
//}
