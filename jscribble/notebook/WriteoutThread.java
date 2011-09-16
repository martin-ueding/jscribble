// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * jscribble is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jscribble.  If not, see <http://www.gnu.org/licenses/>.
 */

package jscribble.notebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import jscribble.Localizer;
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
	 * Works on the queue with tasks. The thread waits for the next task. If
	 * you want to terminate the thread when the queue is empty, you have to
	 * push an empty Task Object into the queue.
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
					NoteBookProgram.log(getClass().getName(),
					        String.format(Localizer.get("Writing %s."),
					                task.getOutfile().getAbsolutePath()));
					ImageIO.write(task.getImg(), "png", new
					        FileOutputStream(task.getOutfile()));
				}
			}
			catch (FileNotFoundException e) {
				NoteBookProgram.handleError(Localizer.get(
				            "Could not find the file to write."));
				e.printStackTrace();
			}
			catch (IOException e) {
				NoteBookProgram.handleError(Localizer.get(
				            "IO error while saving the note image."));
				e.printStackTrace();
			}
			catch (InterruptedException e1) {
				NoteBookProgram.handleError(Localizer.get(
				            "Writing thread was interupted."));
				e1.printStackTrace();
			}
		}

		NoteBookProgram.log(getClass().getName(),
		        Localizer.get("Thread dies."));
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
			NoteBookProgram.handleError(Localizer.get(
			            "Interrupted while scheduling a disk write task."));
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


	public boolean isFileInQueue(File imagefile) {
		for (ImageSwapTask task : tasks) {
			if (task.getOutfile().equals(imagefile))
				return true;
		}
		return false;
	}
}
