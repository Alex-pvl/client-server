package client.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

@XStreamAlias("Image")
public class Image extends Shape implements Serializable {
	@XStreamAsAttribute
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private transient BufferedImage image;

	public Image() {}

	public Image(double x, double y, int width, int height, String path) {
		super(x, y, width, height);
		this.path = path;
		loadImage();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void draw(Graphics g) {
		if (image != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(
				image,
				(int)getX() - 50,
				(int)getY() - 50,
				100,
				100,
				null
			);
		}
	}

	@Override
	public boolean contains(double x, double y) {
		if (image == null) {
			return false;
		}
		Rectangle2D.Double rect = new Rectangle2D.Double(
			getX() - 50,
			getY() - 50,
			getWidth(),
			getHeight()
		);
		return rect.contains(x, y);
	}

	@Override
	public void move() {
		if (!isMoving()) {
			setMoving(true);
			new Thread(() -> {
				double angle = (Math.random()+1) * 2 * Math.PI; // случайный угол движения
				int speed = (int) (Math.random() * 10) + 1; // случайная скорость движения от 1 до 10
				int dx = (int) (speed * Math.cos(angle)); // изменение координаты x
				int dy = (int) (speed * Math.sin(angle)); // изменение координаты y

				while (isMoving()) {
					int width = component.getWidth();
					int height = component.getHeight();
					x += dx;
					y += dy;

					// отскакивание от краев экрана
					if (x < 50) {
						x = 50;
						dx = -dx;
						speed /= 2;
					} else if (x + 50 > width) {
						x = width - 50;
						dx = -dx;
						speed /= 2;
					}
					if (y < 50) {
						y = 50;
						dy = -dy;
						speed /= 2;
					} else if (y + 50 > height) {
						y = height - 50;
						dy = -dy;
						speed /= 2;
					}

					component.repaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	@Override
	public void stop() {
		setMoving(false);
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		super.write(out);
		out.writeUTF(path);
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		super.read(in);
		path = in.readUTF();
		image = ImageIO.read(getClass().getResourceAsStream(path));
	}

	@Override
	public void writeText(BufferedWriter writer) throws IOException {
		super.writeText(writer);
		writer.write(path);
		writer.newLine();
	}

	@Override
	public void readText(BufferedReader reader) throws IOException {
		super.readText(reader);
		path = reader.readLine();
		image = ImageIO.read(getClass().getResourceAsStream(path));
	}

	@Override
	public String toString() {
		return getName() +
			"{x=" + x +
			";y=" + y +
			";path=" + path +
			"}";
	}

	public void loadImage() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
