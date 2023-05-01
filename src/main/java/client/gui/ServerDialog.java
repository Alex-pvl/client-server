package client.gui;

import server.Client;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.net.ServerSocket;

public class ServerDialog extends JDialog {
	public JLabel isConnected;
	public JLabel portLabel;
	public JLabel shapeIdLabel;
	public JTextField portTextField;
	public JCheckBox serverCheckBox;
	public JButton connectBtn;
	public JButton disconnectBtn;
	public JButton clearListBtn;
	public JButton sendNamesBtn;
	public JButton getListSizeBtn;
	public JButton getShapeByIdBtn;
	public JTextField shapeIdTextField;
	public ServerSocket serverSocket;
	public Client client;

	public ServerDialog(ClientFrame parent) {
		super(parent, "Server", false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(220, 300));
		setBounds(0, 0, 220, 300);

		addComponents();
	}

	private void addComponents() {
		isConnected = new JLabel("Status: " + false);
		portLabel = new JLabel("Порт: ");
		portTextField = new JTextField(5);
		shapeIdLabel = new JLabel("Id фигуры: ");
		shapeIdTextField = new JTextField(2);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(isConnected, BorderLayout.NORTH);
		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		portPanel.add(portLabel);
		portPanel.add(portTextField);
		portPanel.add(shapeIdLabel);
		portPanel.add(shapeIdTextField);
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

		getListSizeBtn = new JButton("Get List size");
		getListSizeBtn.setPreferredSize(btnSize);

		getShapeByIdBtn = new JButton("Get Shape");
		getShapeByIdBtn.setPreferredSize(btnSize);

		buttonPanel.add(connectBtn);
		buttonPanel.add(disconnectBtn);
		buttonPanel.add(clearListBtn);
		buttonPanel.add(sendNamesBtn);
		buttonPanel.add(getListSizeBtn);
		buttonPanel.add(getShapeByIdBtn);

		add(buttonPanel, BorderLayout.CENTER);
		setVisible(false);
		setLocationRelativeTo(null);

		serverCheckBox.addActionListener(e -> {
			int port = Integer.parseInt(portTextField.getText().trim());
			if (serverCheckBox.isSelected()) {
				isConnected.setText("Status: " + true);
				System.out.println("Сервер запущен на порту " + port);
				while (true) {
					Server server = new Server((ClientFrame) this.getParent(), port);
					server.start();
				}
			} else {
				isConnected.setText("Status: " + false);
			}
		});
	}

	public void connect() {
		client = new Client(this, Integer.parseInt(portTextField.getText().trim()));
		client.start();
	}

	public void disconnect() {
		client.disconnect();
	}

	public void clear() {
		client.clear();
	}
}
