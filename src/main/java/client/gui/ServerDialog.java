package client.gui;

import client.models.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServerDialog extends JDialog {
	private JLabel isConnected;
	private Client client;
	private JList<String> clients;
	private List<Integer> clientIds;
	private List<String> clientNames;
	private JPanel buttonPanel, clientsPanel, clientNamesPanel;
	private JButton button1,
					button2,
					button3,
					button4,
					button5,
					button6,
					button7,
					button8,
					button9;

	public ServerDialog(ClientFrame parent) {
		super(parent, "Server", false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(500, 400));
		setBounds(0, 0, 500, 400);

		addComponents();
	}

	private void addComponents() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setPreferredSize(new Dimension(260, 400));
		buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(buttonPanel, BorderLayout.EAST);

		button1 = new JButton("Close connection");
		button2 = new JButton("Clear Shapes list");
		button3 = new JButton("Send Shape");
		button4 = new JButton("Send request for Shape");
		button5 = new JButton("Send Shape names");
		button6 = new JButton("Send request for Shapes list size");
		button7 = new JButton("Send request for Shape by id");
		button8 = new JButton("Get receive Shapes list size");
		button9 = new JButton("Update");

		var buttonSize = new Dimension(250, 30);

		button1.setPreferredSize(buttonSize);
		button2.setPreferredSize(buttonSize);
		button3.setPreferredSize(buttonSize);
		button4.setPreferredSize(buttonSize);
		button5.setPreferredSize(buttonSize);
		button6.setPreferredSize(buttonSize);
		button7.setPreferredSize(buttonSize);
		button8.setPreferredSize(buttonSize);
		button9.setPreferredSize(buttonSize);

		button1.setFocusable(false);
		button2.setFocusable(false);
		button3.setFocusable(false);
		button4.setFocusable(false);
		button5.setFocusable(false);
		button6.setFocusable(false);
		button7.setFocusable(false);
		button8.setFocusable(false);
		button9.setFocusable(false);

		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		buttonPanel.add(button4);
		buttonPanel.add(button5);
		buttonPanel.add(button6);
		buttonPanel.add(button7);
		buttonPanel.add(button8);
		buttonPanel.add(button9);
		buttonPanel.setVisible(true);

		clientsPanel = new JPanel(new BorderLayout());
		clientsPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

		clients = new JList<>();

		clientsPanel.add(clients);
		add(clientsPanel);

		connect();
		setVisible(false);
		setLocationRelativeTo(null);
	}

	public void getListOfClients(List<Integer> clientIds, int id) {
		this.clientIds = clientIds;
		String[] clientsList = new String[clientIds.size()];
		for (int i = 0; i < clientIds.size(); i++) {
			if (clientIds.get(i) == id) {
				clientsList[i] = "[*] Клиент №" + clientIds.get(i);
			} else {
				clientsList[i] = "Клиент №" + clientIds.get(i);
			}
		}
		clients.setListData(clientsList);
	}

	public void connect() {
		client = new Client(this);
		client.start();
	}

	public void disconnect() {
		client = null;
		clients.setListData(new String[0]);
	}

	@Override
	public Insets getInsets() {
		return new Insets(40, 20, 20, 5);
	}
}
