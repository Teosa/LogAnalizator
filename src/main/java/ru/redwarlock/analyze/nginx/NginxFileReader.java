package ru.redwarlock.analyze.nginx;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class NginxFileReader implements Runnable {

	private final List<File> files;

	private final BlockingQueue<String> linesRead;

	public NginxFileReader(List<File> files, BlockingQueue<String> linesRead) {
		this.files = files;
		this.linesRead = linesRead;
	}

	@Override
	public void run() {

		for (File file : files) {
			read(file);
		}

	}

	private void read(File file) {
		try (Scanner scanner = new Scanner(file)) {
			//since it is a sample, I avoid the manage of how many lines you have read
			//and that stuff, but it should not be complicated to accomplish
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
