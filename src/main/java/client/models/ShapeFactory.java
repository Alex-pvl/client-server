package client.models;

import java.util.List;

public class ShapeFactory {
	private final List<Shape> availableShapes = List.of(new Polygon(), new Image());

	public Shape createByClassName(String className) {
		for (Shape shape: availableShapes) {
			if (shape.getName().equals(className)) {
				try {
					return shape.getClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					return null;
				}
			}
		}
		return null;
	}
}
