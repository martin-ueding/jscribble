import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.LinkedList;


public class NoteBook {

	private LinkedList<NoteSheet> sheets;
	
	private int currentSheet = 0;
	private NoteSheet current;
	
	private int width;
	private int height;
	
	/**
	 * Creates an empty note book with a single note sheet.
	 * 
	 * @param width width of the individual sheets
	 * @param height height of the individual sheets
	 */
	public NoteBook(int width, int height) {
		this.width = width;
		this.height = height;
		
		sheets = new LinkedList<NoteSheet>();
		
		sheets.add(new NoteSheet(width, height));
		updateCurrrentItem();
	}
	
	private ActionListener doneDrawing;
	
	

	public void setDoneDrawing(ActionListener doneDrawing) {
		this.doneDrawing = doneDrawing;
	}

	public void drawLine(int x, int y, int x2, int y2) {
		current.drawLine(x, y, x2, y2);
		
		if (doneDrawing != null) {
			doneDrawing.actionPerformed(null);
		}
	}
	
	public NoteSheet getCurrentSheet() {
		return sheets.get(currentSheet);
	}
	
	public void forward() {
		if (sheets.size() > currentSheet -1) {
			currentSheet++;
			updateCurrrentItem();
		}
		else if (current.touched()) {
			sheets.add(new NoteSheet(width, height));
			currentSheet++;
			updateCurrrentItem();
		}
	}
	
	public void backward() {
		if (currentSheet > 0) {
			currentSheet--;
			updateCurrrentItem();
		}
	}
	
	private void updateCurrrentItem() {
		current = sheets.get(currentSheet);
		
	}

	public Image getCurrentImage() {
		return current.getImg();
	}
}
