/**
 * Copyright 2013 Thomas Cashman
 */
package org.tilepacker.core;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SpriteSheet;

/**
 * Represents available rectangle space for maxrects algorithm
 * 
 * @author Thomas Cashman
 */
public class Rectangle {
	private int x, y, width, height;
	private Tile[][] tiles;

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void addTiles(SpriteSheet sheet) {
		tiles = new Tile[sheet.getHorizontalCount()][sheet.getVerticalCount()];
		for (int x = 0; x < sheet.getHorizontalCount(); x++) {
			for (int y = 0; y < sheet.getVerticalCount(); y++) {
				tiles[x][y] = new Tile(sheet.getSubImage(x, y));
			}
		}

		width = sheet.getHorizontalCount();
		height = sheet.getVerticalCount();
	}

	public static Rectangle and(Rectangle rect1, Rectangle rect2) {
		if(!rect1.intersects(rect2)) {
			return null;
		}
		
		int x = Math.max(rect1.getX(), rect2.getX());
		int y = Math.max(rect1.getY(), rect2.getY());

		int maxX = Math.min(rect1.getMaxX(), rect2.getMaxX());
		int maxY = Math.min(rect1.getMaxY(), rect2.getMaxY());

		return new Rectangle(x, y, (maxX - x) + 1, (maxY - y) + 1);
	}

	public static List<Rectangle> subtract(Rectangle rect, Rectangle innerRect) {
		List<Rectangle> result = new ArrayList<Rectangle>();

		int leftMinX = Math.min(rect.getX(), innerRect.getX());
		int leftMaxX = Math.max(rect.getX(), innerRect.getX());

		int topMinY = Math.min(rect.getY(), innerRect.getY());
		int topMaxY = Math.max(rect.getY(), innerRect.getY());

		int rightMinX = Math.min(rect.getMaxX(), innerRect.getMaxX());
		int rightMaxX = Math.max(rect.getMaxX(), innerRect.getMaxX());

		int bottomMinY = Math.min(rect.getMaxY(), innerRect.getMaxY());
		int bottomMaxY = Math.max(rect.getMaxY(), innerRect.getMaxY());

		if (leftMinX != leftMaxX) {
			/* There's a rectangle to the left */
			result.add(new Rectangle(leftMinX, topMinY, leftMaxX - leftMinX,
					(bottomMaxY - topMinY) + 1));
		}

		if (topMinY != topMaxY) {
			/* There's a rectangle to the top */
			result.add(new Rectangle(leftMinX, topMinY,
					(rightMaxX - leftMinX) + 1, topMaxY - topMinY));
		}

		if (rightMinX != rightMaxX) {
			/* There's a rectangle to the right */
			result.add(new Rectangle(rightMinX + 1, topMinY,
					rightMaxX - rightMinX, (bottomMaxY - topMinY) + 1));
		}

		if (bottomMinY != bottomMaxY) {
			/* There's a rectangle to the bottom */
			result.add(new Rectangle(leftMinX, bottomMinY + 1,
					(rightMaxX - leftMinX) + 1, bottomMaxY - bottomMinY));
		}
		return result;
	}

	public boolean canContain(SpriteSheet sheet) {
		if (sheet.getHorizontalCount() > width) {
			return false;
		}
		if (sheet.getVerticalCount() > height) {
			return false;
		}
		return true;
	}

	public boolean intersects(Rectangle rect) {
		if (rect.getX() > getMaxX()) {
			return false;
		} else if (rect.getY() > getMaxY()) {
			return false;
		} else if (rect.getMaxX() < getX()) {
			return false;
		} else if (rect.getMaxY() < getY()) {
			return false;
		}
		return true;
	}

	public boolean equals(Rectangle rectangle) {
		return getX() == rectangle.getX() && getY() == rectangle.getY()
				&& getWidth() == rectangle.getWidth()
				&& getHeight() == rectangle.getHeight();
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMaxX() {
		return x + (width - 1);
	}

	public int getMaxY() {
		return y + (height - 1);
	}
}
