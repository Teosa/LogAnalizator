package ru.redwarlock.analyze.onefile;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ResultFileReader implements Runnable {

	private final File file;

	private final BlockingQueue<String> linesRead;

	public ResultFileReader(File file, BlockingQueue<String> linesRead) {
		this.file = file;
		this.linesRead = linesRead;
	}

	@Override
	public void run() {
		try (Scanner scanner = new Scanner(file)) {
			System.out.println("*****  READ " + file.getName() + " START *******");
			int counter = 0;
			while (scanner.hasNext()) {
				try {
					counter++;
					linesRead.put(scanner.nextLine());

					if (counter == 10000) {
						System.out.println("10000 lines read");
						counter = 0;
					}

				} catch (InterruptedException ie) {
					//handle the exception...
					ie.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("*****  READ " + file.getName() + " END *******");
	}

}
