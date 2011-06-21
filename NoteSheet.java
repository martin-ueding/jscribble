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
	 * Highest page number given out so far.
	 */
	private static int pagecount;

	/**
	 * The picture for storing the drawing on.
	 */
	private BufferedImage img;

	private int width;

	private int height;
	
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
	 * Creates an empty note sheet at the end of all other sheets.
	 * 
	 * @param width width of the sheet in pixels
	 * @param height height of the sheet in pixels
	 */
	public NoteSheet(int width, int height) {
		pagenumber = ++pagecount;
		
		this.width = width;
		this.height = height;
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = (Graphics2D)(img.getGraphics());
		g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		g.setColor(new Color(255, 255, 255, 255));
		g.fillRect(0, 0, width, height);
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

}
