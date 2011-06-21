import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

/**
 * Represents a single note sheet.
 */
public class NoteSheet {
	/**
	 * Page number for the current page.
	 */
	private int pagenumber;
	
	/**
	 * Count of pages. Latest page number is pagecount-1.
	 */
	private static int pagecount;

	/**
	 * The picture for storing the drawing on.
	 */
	private BufferedImage img;
	
	private Graphics2D graphics;

	private int width;

	private int height;
	
	/**
	 * Creates an empty note sheet at the end of all other sheets.
	 * 
	 * @param width width of the sheet in pixels
	 * @param height height of the sheet in pixels
	 */
	public NoteSheet(int width, int height) {
		pagenumber = pagecount++;
		
		this.width = width;
		this.height = height;
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D)(img.getGraphics());
		graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		graphics.setColor(new Color(255, 255, 255));
		graphics.fillRect(0, 0, width, height);
	}

	public int getPagenumber() {
		return pagenumber;
	}

	public static int getPagecount() {
		return pagecount;
	}


	public BufferedImage getImg() {
		return img;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Saves the picture to a PNG file.
	 * 
	 * @param outfile filename to use, needs to be png
	 */
	public void saveToFile(String outfile) {
		try {
			javax.imageio.ImageIO.write(img, "png", new FileOutputStream(outfile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drawLine(int x, int y, int x2, int y2) {
		System.out.println("drawing a line from "+x+" "+y);
		touched = true;
		graphics.setColor(new Color(0, 0, 0));
		graphics.drawLine(x, y, x2, y2);
		
	}
	
	private boolean touched = false;

	public boolean touched() {
		return touched;
	}
}
