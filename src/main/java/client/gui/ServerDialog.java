//package client.gui;
//
//import client.models.Image;
//import client.models.Shape;
//import server.udp.Client;
//import server.udp.Server;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import static server.tcp.TCPProtocol.*;
//
//public class ServerDialog extends JDialog {
//	public JLabel isConnected;
//	public JLabel portInLabel;
//	public JLabel portOutLabel;
//	public JLabel shapeIdLabel;
//	public JTextField portInTextField;
//	public JTextField portOutTextField;
//	public JLabel listSizeLabel;
//	public JCheckBox serverCheckBox;
//	public JButton clearListBtn;
//	public JButton sendNamesBtn;
//	public JButton getListSizeBtn;
//	public JButton getShapeByIdBtn;
//	public JButton getAllShapesBtn;
//	public JTextField shapeIdTextField;
//	public Client client;
//
//	public ClientFrame clientFrame;
//
//	public ServerDialog(ClientFrame parent) {
//		super(parent, "Server (UDP)", false);
//		this.clientFrame = parent;
//		setLayout(new BorderLayout());
//		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		setPreferredSize(new Dimension(430, 200));
//		setBounds(0, 0, 430, 200);
//
//		addComponents();
//	}
//
//	private void addComponents() {
//		isConnected = new JLabel("Status: " + false);
//		portInLabel = new JLabel("Server Port: ");
//		portInTextField = new JTextField(5);
//		portOutLabel = new JLabel("Client Port: ");
//		portOutTextField = new JTextField(5);
//		shapeIdLabel = new JLabel("Shape ID: ");
//		shapeIdTextField = new JTextField(2);
//		listSizeLabel = new JLabel("size: " + clientFrame.shapes.size());
//
//		JPanel topPanel = new JPanel(new BorderLayout());
//		topPanel.add(isConnected, BorderLayout.NORTH);
//		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		portPanel.add(portInLabel);
//		portPanel.add(portInTextField);
//		portPanel.add(portOutLabel);
//		portPanel.add(portOutTextField);
//		portPanel.add(shapeIdLabel);
//		portPanel.add(shapeIdTextField);
//		portPanel.add(listSizeLabel);
//		topPanel.add(portPanel, BorderLayout.CENTER);
//
//		serverCheckBox = new JCheckBox("Запустить сервер");
//		topPanel.add(serverCheckBox, BorderLayout.SOUTH);
//
//		add(topPanel, BorderLayout.NORTH);
//
//		JPanel buttonPanel = new JPanel();
//		Dimension btnSize = new Dimension(200, 25);
//
//		clearListBtn = new JButton("Clear List");
//		clearListBtn.setPreferredSize(btnSize);
//		clearListBtn.addActionListener(e -> {
//			int portIn = Integer.parseInt(portInTextField.getText().trim());
//			int portOut = Integer.parseInt(portOutTextField.getText().trim());
//
//			client = new Client(this, clientFrame, portOut, portIn);
//			client.start();
//		});
//
//		sendNamesBtn = new JButton("Send Names");
//		sendNamesBtn.setPreferredSize(btnSize);
//		//sendNamesBtn.addActionListener(e -> sendShapeNames());
//
//		getListSizeBtn = new JButton("Get List size");
//		getListSizeBtn.setPreferredSize(btnSize);
//		//getListSizeBtn.addActionListener(e -> getLSize());
//
//		getAllShapesBtn = new JButton("Get All Shapes");
//		getAllShapesBtn.setPreferredSize(btnSize);
//		//getAllShapesBtn.addActionListener(e -> getAll());
//
//		getShapeByIdBtn = new JButton("Get Shape");
//		getShapeByIdBtn.setPreferredSize(btnSize);
//		//getShapeByIdBtn.addActionListener(e -> getById());
//
//		buttonPanel.add(clearListBtn);
//		buttonPanel.add(sendNamesBtn);
//		buttonPanel.add(getListSizeBtn);
//		buttonPanel.add(getShapeByIdBtn);
//		buttonPanel.add(getAllShapesBtn);
//
//		add(buttonPanel, BorderLayout.CENTER);
//		setVisible(false);
//		setLocationRelativeTo(null);
//
//		serverCheckBox.addActionListener(e -> {
//			int portIn = Integer.parseInt(portInTextField.getText().trim());
//			int portOut = Integer.parseInt(portInTextField.getText().trim());
//			if (serverCheckBox.isSelected()) {
//				isConnected.setText("Status: " + true);
//				Server server = new Server(clientFrame, portOut, portIn);
//				server.start();
//			} else {
//				isConnected.setText("Status: " + false);
//			}
//		});
//	}
//
////	public void connect() {
////		int portOut = Integer.parseInt(portOutTextField.getText().trim());
////		isConnected.setText("Status: " + true);
////		client = new Client(this, clientFrame, clientPort);
////		client.start();
////	}
////
////	public void disconnect() {
////		try {
////			closeConnection(client.s);
////			isConnected.setText("Status: " + false);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
////
////	public void clear() {
////		try {
////			clearList(client.s);
////			clientFrame.drawingPanel.repaint();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
////
////	public void getLSize() {
////		try {
////			getListSize(client.s);
////			listSizeLabel.setText("size: " + client.clientFrame.shapes.size());
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
////
////	public void getAll() {
////		try {
////			getAllShapes(client.s);
////			clientFrame.shapes.forEach(s -> {
////				if (s instanceof Image) {
////					((Image) s).loadImage();
////				}
////			});
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
////
////	public void getById() {
////		int id = Integer.parseInt(shapeIdTextField.getText().trim());
////		try {
////			TCPProtocol.getShape(client.s, id);
////			clientFrame.shapes.forEach(s -> {
////				if (s instanceof Image) {
////					((Image) s).loadImage();
////				}
////			});
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
////
////	public void sendShapeNames() {
////		try {
////			var names = clientFrame.shapes.stream()
////				.map(Shape::toString)
////				.toList();
////
////			var list = new ArrayList<>(names);
////
////			sendAllShapeNames(client.s, list);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
//}
