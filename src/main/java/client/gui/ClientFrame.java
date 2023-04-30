package client.gui;

import client.filters.*;
import client.models.*;
import client.models.Image;
import client.models.Polygon;
import client.models.Shape;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ClientFrame extends JFrame {

	private static final String POLYGON = "Polygon";
	private static final String IMAGE = "Image";

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem saveTextMenuItem,
					loadTextMenuItem,
					saveBinaryMenuItem,
					loadBinaryMenuItem,
					saveObjectMenuItem,
					loadObjectMenuItem,
					saveXMLMenuItem,
					loadXMLMenuItem;
	private JPanel comboBoxPanel;
	private MyPanel drawingPanel;
	private JComboBox<String> shapeTypeSelection;
	private JTextField textField;
	private JLabel verticesLabel;
	private JButton startAllButton,
					stopAllButton,
					startTypeButton,
					stopTypeButton;
	private JPanel buttonPanel;
	private JCheckBox initializeConnectionCheckBox;
	@XStreamAsAttribute
	private List<Shape> shapes;
	private ShapeFactory shapeFactory;
	private ServerDialog serverDialog;
	private boolean isConnected = false;

	public ClientFrame() throws IOException {
		super("Client");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(900, 600));

		addComponents();
		addListeners();
	}

	@Override
	public Insets getInsets() {
		return new Insets(10, 20, 10, 20);
	}

	private class MyPanel extends JPanel {

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (Shape shape: shapes) {
				shape.draw(g);
			}
		}
	}

	private void addComponents() {
		shapes = new ArrayList<>();
		shapeFactory = new ShapeFactory();

		initializeConnectionCheckBox = new JCheckBox("Init server connection");
		// Создание выпадающего списка для выбора типа фигур
		shapeTypeSelection = new JComboBox<>(new String[]{
			POLYGON,
			IMAGE
		});
		shapeTypeSelection.setSelectedIndex(0);
		shapeTypeSelection.setPreferredSize(new Dimension(150, 25));

		// Создание строки для ввода числа вершин многоугольника
		verticesLabel = new JLabel("Enter vertices count:");
		textField = new JTextField("3", 3);
		textField.setPreferredSize(new Dimension(150, 25));

		// Создание меню и его элементов
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		saveTextMenuItem = new JMenuItem("Save in .txt file");
		loadTextMenuItem = new JMenuItem("Load from .txt file");
		saveBinaryMenuItem = new JMenuItem("Save in .dat file");
		loadBinaryMenuItem = new JMenuItem("Load from .dat file");
		saveObjectMenuItem = new JMenuItem("Save in .serial file");
		loadObjectMenuItem = new JMenuItem("Load from .serial file");
		saveXMLMenuItem = new JMenuItem("Save in XML-file");
		loadXMLMenuItem = new JMenuItem("Load from XML-file");

		// Добавление элементов в меню
		fileMenu.add(saveTextMenuItem);
		fileMenu.add(loadTextMenuItem);
		fileMenu.add(saveBinaryMenuItem);
		fileMenu.add(loadBinaryMenuItem);
		fileMenu.add(saveObjectMenuItem);
		fileMenu.add(loadObjectMenuItem);
		fileMenu.add(saveXMLMenuItem);
		fileMenu.add(loadXMLMenuItem);
		menuBar.add(fileMenu);

		// Добавление выпадающего списка, и текстового поля
		comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		comboBoxPanel.add(shapeTypeSelection);
		comboBoxPanel.add(verticesLabel);
		comboBoxPanel.add(textField);
		comboBoxPanel.add(initializeConnectionCheckBox);

		// Создание главной панели для рисовки объектов
		drawingPanel = new MyPanel();
		drawingPanel.setBackground(Color.WHITE);
		drawingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

		// Добавление кнопок запуска и остановы
		startAllButton = new JButton("Start (all)");
		stopAllButton = new JButton("Stop (all)");
		startTypeButton = new JButton("Start (selected type)");
		stopTypeButton = new JButton("Stop (selected type)");

		// Создание панели для кнопок
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(startAllButton);
		buttonPanel.add(stopAllButton);
		buttonPanel.add(startTypeButton);
		buttonPanel.add(stopTypeButton);

		// Добавление компонентов
		add(comboBoxPanel, BorderLayout.NORTH);
		add(drawingPanel);
		add(buttonPanel, BorderLayout.SOUTH);
		setJMenuBar(menuBar);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				shapes.forEach(Shape::stop);
			}
		});

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addListeners() throws IOException {
		// Движения
		startAllButton.addActionListener(e -> performMovement("start", true));
		startTypeButton.addActionListener(e -> performMovement("start", false));
		stopAllButton.addActionListener(e -> performMovement("stop", true));
		stopTypeButton.addActionListener(e -> performMovement("stop", false));
		addMouseListenerToDrawingPanel();

		// Сериализация
		saveTextMenuItem.addActionListener(e -> saveToTxt());
		loadTextMenuItem.addActionListener(e -> loadFromTxt());
		saveBinaryMenuItem.addActionListener(e -> saveToBinary());
		loadBinaryMenuItem.addActionListener(e -> loadFromBinary());
		saveObjectMenuItem.addActionListener(e -> saveToSerial());
		loadObjectMenuItem.addActionListener(e -> loadFromSerial());
		saveXMLMenuItem.addActionListener(e -> saveToXML());
		loadXMLMenuItem.addActionListener(e -> loadFromXML());

		// сервер
		initServer();
	}

	private void initServer() {
		initializeConnectionCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				serverDialog = new ServerDialog(this);
				serverDialog.setVisible(true);
			} else {
				initializeConnectionCheckBox.setSelected(false);
				serverDialog.setVisible(false);
			}
		});
	}

	private void addMouseListenerToDrawingPanel() {
		drawingPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					String selectedShapeType = (String) shapeTypeSelection.getSelectedItem();
					Shape shape = null;
					if (Objects.equals(selectedShapeType, "Polygon")) {
						int vertices = Integer.parseInt(textField.getText().trim());
						shape = new Polygon(e.getX(), e.getY(), vertices);
					} else if (Objects.equals(selectedShapeType, "Image")) {
						shape = new Image(e.getX(), e.getY(),
							100, 100,
							"/resources/" + (int) (Math.random() * 11 + 1) + ".png"
						);
					}
					if (shape != null) {
						shape.setComponent(drawingPanel);
						shape.draw(drawingPanel.getGraphics());
						shapes.add(shape);
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					for (Shape shape: shapes) {
						if (shape.contains(e.getX(), e.getY())) {
							shape.stop();
							shapes.remove(shape);
							drawingPanel.repaint();
							break;
						}
					}
				}
			}
		});
	}

	private void saveToTxt() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new TXTFileFilter());
		int returnValue = fileChooser.showSaveDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile, StandardCharsets.UTF_8))) {
				int shapesCount = shapes.size();
				writer.write(""+shapesCount);
				writer.newLine();
				for (Shape shape: shapes) {
					writer.write(shape.getName());
					writer.newLine();
					shape.writeText(writer);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void loadFromTxt() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new TXTFileFilter());
		int returnValue = fileChooser.showOpenDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile, StandardCharsets.UTF_8))) {
				shapes.clear();
				int shapesCount = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < shapesCount; i++) {
					String shapeName = reader.readLine().trim();
					Shape shape = shapeFactory.createByClassName(shapeName);
					shape.readText(reader);
					shapes.add(shape);
				}
				shapes.forEach(s -> {
					if (s instanceof Image) {
						((Image) s).loadImage();
					}
					s.setComponent(drawingPanel);
				});
				drawingPanel.repaint();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void saveToBinary() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new BinaryFileFilter());
		int returnValue = fileChooser.showSaveDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try(DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile))) {
				int shapesCount = shapes.size();
				out.writeInt(shapesCount);
				for (Shape shape: shapes) {
					out.writeUTF(shape.getName());
					shape.write(out);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void loadFromBinary() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new BinaryFileFilter());
		int returnValue = fileChooser.showOpenDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try(DataInputStream in = new DataInputStream(new FileInputStream(selectedFile))) {
				shapes.clear();
				int shapesCount = in.readInt();
				for (int i = 0; i < shapesCount; i++) {
					String shapeName = in.readUTF();
					Shape shape = shapeFactory.createByClassName(shapeName);
					shape.read(in);
					shapes.add(shape);
				}
				shapes.forEach(s -> {
					if (s instanceof Image) {
						((Image) s).loadImage();
					}
					s.setComponent(drawingPanel);
				});
				drawingPanel.repaint();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void saveToSerial() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new SerialFileFilter());
		int returnValue = fileChooser.showSaveDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(selectedFile))) {
				out.writeObject(shapes);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void loadFromSerial() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new SerialFileFilter());
		int returnValue = fileChooser.showSaveDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(selectedFile))) {
				shapes.clear();
				ArrayList<Shape> loadedShapes = (ArrayList<Shape>) in.readObject();
				shapes.addAll(loadedShapes);
				shapes.forEach(s -> {
					if (s instanceof Image) {
						((Image) s).loadImage();
					}
					s.setComponent(drawingPanel);
				});
				drawingPanel.repaint();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void saveToXML() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new XMLFileFilter());
		int returnValue = fileChooser.showSaveDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			XStream xstream = new XStream();
			xstream.allowTypeHierarchy(Polygon.class);
			xstream.allowTypeHierarchy(Image.class);
			try(FileWriter writer = new FileWriter(selectedFile)) {
				xstream.toXML(shapes, writer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void loadFromXML() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new XMLFileFilter());
		int returnValue = fileChooser.showSaveDialog(ClientFrame.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			XStream xstream = new XStream();
			xstream.allowTypeHierarchy(Polygon.class);
			xstream.allowTypeHierarchy(Image.class);
			try(FileReader reader = new FileReader(selectedFile)) {
				shapes.clear();
				shapes = (List<Shape>) xstream.fromXML(reader);
				shapes.forEach(s -> {
					if (s instanceof Image) {
						((Image) s).loadImage();
					}
					s.setComponent(drawingPanel);
				});
				drawingPanel.repaint();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void performMovement(String mode, boolean all) {
		String type = getShapeTypeFromSelector();

		if (!all) {
			if (type.equals(POLYGON)) {
				shapes.stream()
					.filter(s -> s.getName()
						.equals(Polygon.class.getSimpleName())
					)
					.forEach(Objects.requireNonNull(action(mode)));
			} else if (type.equals(IMAGE)) {
				shapes.stream()
					.filter(s -> s.getName()
						.equals(Image.class.getSimpleName())
					)
					.forEach(Objects.requireNonNull(action(mode)));
			}
		} else shapes.forEach(action(mode));
	}

	private Consumer<? super Shape> action(String mode) {
		if (mode.equals("start")) {
			return Shape::move;
		} else if (mode.equals("stop")) {
			return Shape::stop;
		}
		return null;
	}

	private String getShapeTypeFromSelector() {
		return Objects.requireNonNull(
			shapeTypeSelection.getSelectedItem()
		).toString();
	}
}
