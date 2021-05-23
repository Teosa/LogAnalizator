package ru.redwarlock.analyze.nginx;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class NginxFileProcessor implements Runnable {

	private final BlockingQueue<String> linesToProcess;

	private final BlockingQueue<String> findedLines;

	public NginxFileProcessor(BlockingQueue<String> linesToProcess, BlockingQueue<String> findedLines) {
		this.linesToProcess = linesToProcess;
		this.findedLines = findedLines;
	}

	@Override
	public void run() {
		String line = "";
		Pattern svoedomPattern = Pattern.compile("svoedom\\.ru");
		Pattern linePattern = Pattern.compile("\\[[0-9]{2}\\/[A-Za-z]{3}\\/[0-9]{4}.*(\\t404|\\t408|\\/500)\\s.*HTTP\\/1.1\\\"");
		int counter = 0;
		System.out.println("*****  OPERATE START *******");
		try {
			while ((line = linesToProcess.take()) != null) {
				counter++;

				if (svoedomPattern.matcher(line).find() && linePattern.matcher(line).find()) {
					findedLines.add(line);
				}

				if (counter == 10000) {
					System.out.println("10000 lines operated");
					counter = 0;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("*****  OPERATE END *******");
	}

}
