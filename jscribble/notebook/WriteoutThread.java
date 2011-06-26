// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.notebook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import jscribble.NoteBookProgram;

/**
 * In order to make the user interface faster, the writing of unneeded images
 * is put into a second thread. That way, the UI does not freeze during the
 * little IO time.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class WriteoutThread extends Thread {
	/**
	 * A queue with the tasks that this thread has to save to disk.
	 */
	private LinkedBlockingQueue<ImageSwapTask> tasks;


	/**
	 * This flag stops the thread after the last item in the queue. If not set,
	 * the thread will wait for items in the queue to save.
	 */
	private boolean stopAfterLastItem = false;


	/**
	 * Creates and starts the thread.
	 */
	public WriteoutThread() {
		tasks = new LinkedBlockingQueue<ImageSwapTask>();
		this.start();
	}


	/**
	 * Schedules a new task.
	 *
	 * @param t task to schedule
	 */
	public void schedule(ImageSwapTask t) {
		try {
			tasks.put(t);
		}
		catch (InterruptedException e) {
			NoteBookProgram.handleError("Interrupted while scheduling a disk write task.");
			e.printStackTrace();
		}
	}


	/**
	 * Stops the thread's daemon mode and lets it die when the queue is empty.
	 */
	public void stopAfterLast() {
		stopAfterLastItem = true;
		schedule(new ImageSwapTask(null, null));
	}


	/**
	 * Works on the queue with tasks. The thread waits for the next task. If you
	 * want to terminate the thread when the queue is empty, you have to push an
	 * empty Task Object into the queue.
	 */
	public void run() {
		ImageSwapTask task;
		while (!stopAfterLastItem || !tasks.isEmpty()) {
			try {
				if (stopAfterLastItem) {
					task = tasks.poll();
				}
				else {
					task = tasks.take();
				}

				if (task.getImg() != null) {
					NoteBookProgram.log(getClass().getName(), String.format("Writing %s.", task.getOutfile().getAbsolutePath()));
					ImageIO.write(task.getImg(), "png", new FileOutputStream(task.getOutfile()));
				}
			}
			catch (FileNotFoundException e) {
				NoteBookProgram.handleError("Could not find the file to write.");
				e.printStackTrace();
			}
			catch (IOException e) {
				NoteBookProgram.handleError("IO error while saving the note image.");
				e.printStackTrace();
			}
			catch (InterruptedException e1) {
				NoteBookProgram.handleError("Writing thread was interupted.");
				e1.printStackTrace();
			}
		}

		NoteBookProgram.log(getClass().getName(), "thread dies");
	}
}