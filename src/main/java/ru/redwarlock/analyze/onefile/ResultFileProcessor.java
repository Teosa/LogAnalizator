package ru.redwarlock.analyze.onefile;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultFileProcessor implements Runnable {

	private final BlockingQueue<String> linesToProcess;

	private final BlockingQueue<String> findedLines;

	public ResultFileProcessor(BlockingQueue<String> linesToProcess, BlockingQueue<String> findedLines) {
		this.linesToProcess = linesToProcess;
		this.findedLines = findedLines;
	}

	private final Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{4}");
	private final Pattern urlPattern = Pattern.compile("(PUT|POST|GET|DELETE)\\s\\/.*HTTP\\/1\\.1");
	private Long ERR_404 = 0L;
	private Long ERR_408 = 0L;
	private Long ERR_500 = 0L;

	@Override
	public void run() {
		String line = "";
		int counter = 0;
		System.out.println("*****  OPERATE START *******");
		try {
			while ((line = linesToProcess.poll(5, TimeUnit.SECONDS)) != null) {
				counter++;

				findedLines.add(operate(line));

				if (counter == 10000) {
					System.out.println("10000 lines operated");
					counter = 0;
				}
			}

			System.out.println(counter + " lines operated");
			System.out.println("**************************");
			System.out.println("404: " + ERR_404);
			System.out.println("408: " + ERR_408);
			System.out.println("500: " + ERR_500);
			System.out.println("**************************");


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("*****  OPERATE END *******");
	}

	private String operate(String line) {
		String[] parts = line.split("\\|");
		if(parts.length < 3) {
			return "ERROR";
		}

		try {
			int errorCode = Integer.parseInt(parts[1]);
			if(errorCode == 404) { ERR_404++; }
			if(errorCode == 408) { ERR_408++; }
			if(errorCode == 500) { ERR_500++; }
		} catch (NumberFormatException nfe) {
			return "";
		}


		String ipString = "";
		Matcher ipMatcher = ipPattern.matcher(parts[2]);
		if (ipMatcher.find()) {
			ipString = ipMatcher.group(0);
		}

		String urlString = "";
		Matcher urlMatcher = urlPattern.matcher(parts[2]);
		if (urlMatcher.find()) {
			urlString = urlMatcher.group(0);
		}

		return
//				parts[0]
//				+ "|"
				parts[1]
				//+ "|"
				//+ ipString
				+ "|"
				+ urlString
//				+ "|"
//				+ parts[2]
		;
	}

}
