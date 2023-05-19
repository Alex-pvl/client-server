package client.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.*;

@XStreamAlias("Shape")
public abstract class Shape implements Serializable {
	public static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	protected double x;
	@XStreamAsAttribute
	protected double y;
	private transient boolean moving;
	@XStreamAsAttribute
	protected int width;
	@XStreamAsAttribute
	protected int height;
	@XStreamAsAttribute
	protected transient Color color;
	protected int color_r;
	protected int color_g;
	protected int color_b;

	public int getColor_r() {
		return color_r;
	}

	public void setColor_r(int color_r) {
		this.color_r = color_r;
	}

	public int getColor_g() {
		return color_g;
	}

	public void setColor_g(int color_g) {
		this.color_g = color_g;
	}

	public int getColor_b() {
		return color_b;
	}

	public void setColor_b(int color_b) {
		this.color_b = color_b;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected String name;
	protected transient Graphics graphics;
	protected transient Component component;

	public Shape() {
		color_r = (int) (Math.random() * 255);
		color_g = (int) (Math.random() * 255);
		color_b = (int) (Math.random() * 255);
		color = new Color(
			color_r,
			color_g,
			color_b
		);
	}

	public Shape(double x, double y, int width, int height) {
		this.name = getName();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		color_r = (int) (Math.random() * 255);
		color_g = (int) (Math.random() * 255);
		color_b = (int) (Math.random() * 255);
		color = new Color(
			color_r,
			color_g,
			color_b
		);
	}

	public abstract String getName();
	public abstract void draw(Graphics g);
	public abstract boolean contains(double x, double y);

	public abstract void move();
	public abstract void stop();

	public void write(DataOutputStream out) throws IOException {
		out.writeDouble(x);
		out.writeDouble(y);
		out.writeInt(width);
		out.writeInt(height);
		out.writeInt(color.getRed());
		out.writeInt(color.getGreen());
		out.writeInt(color.getBlue());
	}

	public void read(DataInputStream in) throws IOException {
		x = in.readDouble();
		y = in.readDouble();
		width = in.readInt();
		height = in.readInt();
		int r = in.readInt();
		int g = in.readInt();
		int b = in.readInt();
		color = new Color(r, g, b);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void writeText(BufferedWriter writer) throws IOException {
		writeLine(writer, x, y, width, height, color.getRed(), color.getGreen(), color.getBlue());
	}

	public void readText(BufferedReader reader) throws IOException {
		x = Double.parseDouble(reader.readLine());
		y = Double.parseDouble(reader.readLine());
		width = Integer.parseInt(reader.readLine());
		height = Integer.parseInt(reader.readLine());
		int r = Integer.parseInt(reader.readLine());
		int g = Integer.parseInt(reader.readLine());
		int b = Integer.parseInt(reader.readLine());
		color = new Color(r, g, b);
	}

	@Override
	public String toString() {
		return getName() + "{x=" + x + ", y=" + y + "}";
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public Color getColor() {
		return color;
	}

	public Graphics getGraphics() {
		return graphics;
	}
	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
		graphics = component.getGraphics();
	}

	private void writeLine(BufferedWriter writer, Number... fields) throws IOException {
		for (Number field: fields) {
			writer.write("" + field);
			writer.newLine();
		}
	}
}
