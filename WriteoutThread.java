// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class WriteoutThread extends Thread {
	private LinkedBlockingQueue<ImageSwapTask> tasks;
	
	public WriteoutThread() {
		tasks = new LinkedBlockingQueue<ImageSwapTask>();
		this.start();
	}
	
	public void schedule(ImageSwapTask t) {
		tasks.add(t);
	}
	
	public void run() {
		while (true) {
			ImageSwapTask task;
			try {
				task = tasks.take();
				
				assert(task.getImg() != null);
				assert(task.getOutfile() != null);
				//assert(filename.canWrite());

				System.out.println("writing out " + task.getOutfile().getCanonicalPath());
				javax.imageio.ImageIO.write(task.getImg(), "png", new FileOutputStream(task.getOutfile()));
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
