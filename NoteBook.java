import java.awt.event.ActionListener;
import java.util.LinkedList;


public class NoteBook {
	private LinkedList<NoteSheet> sheets;

	private int currentSheet = 0;

	private NoteSheet current;
	
	private int width;
	private int height;
	
	private ActionListener doneDrawing;

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
	
	public void drawLine(int x, int y, int x2, int y2) {
		current.drawLine(x, y, x2, y2);
		
		fireDoneDrawing();
	}

	public void setDoneDrawing(ActionListener doneDrawing) {
		this.doneDrawing = doneDrawing;
	}
	
	private void fireDoneDrawing() {
		if (doneDrawing != null) {
			doneDrawing.actionPerformed(null);
		}
	}

	public void forward() {
		if (sheets.size() <= currentSheet -1) {
		}
		else if (current.touched()) {
			sheets.add(new NoteSheet(width, height));
		}
		else {
			return;
		}
		currentSheet++;
		updateCurrrentItem();
		fireDoneDrawing();
	}
	
	public void backward() {
		if (currentSheet > 0) {
			currentSheet--;
			updateCurrrentItem();
			fireDoneDrawing();
		}
	}
	
	public NoteSheet getCurrentSheet() {
		return sheets.get(currentSheet);
	}

	private void updateCurrrentItem() {
		assert(currentSheet >= 0);
		assert(currentSheet < sheets.size());
		current = sheets.get(currentSheet);
	}
}
