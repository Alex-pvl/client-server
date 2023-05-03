package client.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.*;

@XStreamAlias("Polygon")
public class Polygon extends Shape implements Serializable {
	@XStreamAsAttribute
	private int n;
	@XStreamAsAttribute
	private int radius;
	private int[] xPoints;
	private int[] yPoints;
	private transient boolean increasing;

	public Polygon() {}

	public Polygon(double x, double y, int n) {
		super(x, y, 0, 0);
		radius = (int) (Math.random() * 100 + 1);
		width = height = radius * 2;
		this.n = n;
		xPoints = new int[n];
		yPoints = new int[n];
		setPoints();
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void draw(Graphics g) {
		setPoints();
		g.setColor(getColor());
		g.fillPolygon(xPoints, yPoints, n);
		g.setColor(Color.BLACK);
		g.drawPolygon(xPoints, yPoints, n);
	}

	@Override
	public boolean contains(double x, double y) {
		java.awt.Polygon tmp = new java.awt.Polygon(xPoints, yPoints, n);
		return tmp.contains(x, y);
	}

	@Override
	public void move() {
		if (!isMoving()) {
			setMoving(true);
			new Thread(() -> {
				while (isMoving()) {
					if (increasing) {
						radius++;
					} else {
						radius--;
					}
					component.repaint();
					if (radius <= 0 || radius >= 100) {
						increasing = !increasing;
					}
					this.width = this.height = radius * 2;
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
		out.writeInt(n);
		out.writeInt(radius);
		for (int i = 0; i < n; i++) {
			out.writeInt(xPoints[i]);
		}
		for (int i = 0; i < n; i++) {
			out.writeInt(yPoints[i]);
		}
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		super.read(in);
		n = in.readInt();
		xPoints = new int[n];
		yPoints = new int[n];
		radius = in.readInt();
		for (int i = 0; i < n; i++) {
			xPoints[i] = in.readInt();
		}
		for (int i = 0; i < n; i++) {
			yPoints[i] = in.readInt();
		}
	}

	@Override
	public void writeText(BufferedWriter writer) throws IOException {
		super.writeText(writer);
		writer.write(""+n);
		writer.newLine();
		writer.write(""+radius);
		writer.newLine();
		for (int i = 0; i < n; i++) {
			writer.write(""+xPoints[i]);
			writer.newLine();
		}
		for (int i = 0; i < n; i++) {
			writer.write(""+yPoints[i]);
			writer.newLine();
		}
	}

	@Override
	public void readText(BufferedReader reader) throws IOException {
		super.readText(reader);
		n = Integer.parseInt(reader.readLine());
		xPoints = new int[n];
		yPoints = new int[n];
		radius = Integer.parseInt(reader.readLine());
		for (int i = 0; i < n; i++) {
			xPoints[i] = Integer.parseInt(reader.readLine());
		}
		for (int i = 0; i < n; i++) {
			yPoints[i] = Integer.parseInt(reader.readLine());
		}
	}

	@Override
	public String toString() {
		return getName() +
			"{x=" + x +
			";y=" + y +
			";n=" + n +
			";radius=" + radius +
			"}";
	}

	private void setPoints() {
		for (int i = 0; i < n; i++) {
			double angle = 2 * Math.PI * i / n;
			xPoints[i] = (int) (getX() + radius * Math.cos(angle));
			yPoints[i] = (int) (getY() + radius * Math.sin(angle));
		}
	}
}
