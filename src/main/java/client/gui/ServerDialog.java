package client.gui;

import client.models.Image;
import client.models.Shape;
import server.Client;
import server.Server;
import server.TCPProtocol;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static server.TCPProtocol.*;

public class ServerDialog extends JDialog {
	public JLabel isConnected;
	public JLabel portLabel;
	public JLabel shapeIdLabel;
	public JTextField portTextField;
	public JLabel listSizeLabel;
	public JCheckBox serverCheckBox;
	public JButton connectBtn;
	public JButton disconnectBtn;
	public JButton clearListBtn;
	public JButton sendNamesBtn;
	public JButton getListSizeBtn;
	public JButton getShapeByIdBtn;
	public JButton getAllShapesBtn;
	public JTextField shapeIdTextField;
	public Client client;

	public ClientFrame clientFrame;

	public ServerDialog(ClientFrame parent) {
		super(parent, "Server", false);
		this.clientFrame = parent;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(260, 340));
		setBounds(0, 0, 260, 340);

		addComponents();
	}

	private void addComponents() {
		isConnected = new JLabel("Status: " + false);
		portLabel = new JLabel("Порт: ");
		portTextField = new JTextField(5);
		shapeIdLabel = new JLabel("Shape ID: ");
		shapeIdTextField = new JTextField(2);
		listSizeLabel = new JLabel("size: " + clientFrame.shapes.size());

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(isConnected, BorderLayout.NORTH);
		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		portPanel.add(portLabel);
		portPanel.add(portTextField);
		portPanel.add(shapeIdLabel);
		portPanel.add(shapeIdTextField);
		portPanel.add(listSizeLabel);
		topPanel.add(portPanel, BorderLayout.CENTER);

		serverCheckBox = new JCheckBox("Запустить сервер");
		topPanel.add(serverCheckBox, BorderLayout.SOUTH);

		add(topPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		Dimension btnSize = new Dimension(200, 25);

		connectBtn = new JButton("Connect");
		connectBtn.setPreferredSize(btnSize);
		connectBtn.addActionListener(e -> connect());

		disconnectBtn = new JButton("Disconnect");
		disconnectBtn.setPreferredSize(btnSize);
		disconnectBtn.addActionListener(e -> disconnect());

		clearListBtn = new JButton("Clear List");
		clearListBtn.setPreferredSize(btnSize);
		clearListBtn.addActionListener(e -> clear());

		sendNamesBtn = new JButton("Send Names");
		sendNamesBtn.setPreferredSize(btnSize);
		sendNamesBtn.addActionListener(e -> sendShapeNames());

		getListSizeBtn = new JButton("Get List size");
		getListSizeBtn.setPreferredSize(btnSize);
		getListSizeBtn.addActionListener(e -> getLSize());

		getAllShapesBtn = new JButton("Get All Shapes");
		getAllShapesBtn.setPreferredSize(btnSize);
		getAllShapesBtn.addActionListener(e -> getAll());

		getShapeByIdBtn = new JButton("Get Shape");
		getShapeByIdBtn.setPreferredSize(btnSize);
		getShapeByIdBtn.addActionListener(e -> getById());

		buttonPanel.add(connectBtn);
		buttonPanel.add(disconnectBtn);
		buttonPanel.add(clearListBtn);
		buttonPanel.add(sendNamesBtn);
		buttonPanel.add(getListSizeBtn);
		buttonPanel.add(getShapeByIdBtn);
		buttonPanel.add(getAllShapesBtn);

		add(buttonPanel, BorderLayout.CENTER);
		setVisible(false);
		setLocationRelativeTo(null);

		serverCheckBox.addActionListener(e -> {
			int port = Integer.parseInt(portTextField.getText().trim());
			if (serverCheckBox.isSelected()) {
				isConnected.setText("Status: " + true);
				Server server = new Server(clientFrame, port);
				server.start();
			} else {
				isConnected.setText("Status: " + false);
			}
		});
	}

	public void connect() {
		int port = Integer.parseInt(portTextField.getText().trim());
		isConnected.setText("Status: " + true);
		client = new Client(this, clientFrame, port);
		client.start();
	}

	public void disconnect() {
		try {
			closeConnection(client.s);
			isConnected.setText("Status: " + false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		try {
			clearList(client.s);
			clientFrame.drawingPanel.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getLSize() {
		try {
			getListSize(client.s);
			listSizeLabel.setText("size: " + client.clientFrame.shapes.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getAll() {
		try {
			getAllShapes(client.s);
			clientFrame.shapes.forEach(s -> {
				if (s instanceof Image) {
					((Image) s).loadImage();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getById() {
		int id = Integer.parseInt(shapeIdTextField.getText().trim());
		try {
			TCPProtocol.getShape(client.s, id);
			clientFrame.shapes.forEach(s -> {
				if (s instanceof Image) {
					((Image) s).loadImage();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendShapeNames() {
		try {
			var names = clientFrame.shapes.stream()
				.map(Shape::toString)
				.toList();

			var list = new ArrayList<>(names);

			sendAllShapeNames(client.s, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
