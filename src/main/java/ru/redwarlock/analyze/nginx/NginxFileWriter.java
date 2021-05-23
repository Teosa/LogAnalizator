package ru.redwarlock.analyze.nginx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxFileWriter implements Runnable {

	private final BlockingQueue<String> findedLines;

	public NginxFileWriter(BlockingQueue<String> findedLines) {
		this.findedLines = findedLines;
	}

	@Override
	public void run() {

		System.out.println("*****  WRITE START *******");
		try (FileWriter fw = new FileWriter("myfile.txt", true)) {
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			String line = "";
			int counter = 0;
			out.println("ДАТА|КОД ОШИБКИ|ТЕКСТ ОШИБКИ");

			while ((line = findedLines.take()) != null) {
				counter++;

				out.println(formatLine(line));

				if (counter == 1000) {
					System.out.println("1000 lines wrote");
					counter = 0;
				}
			}

			out.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("*****  WRITE END *******");
	}

	private String formatLine(String input) {
		Pattern datePattern = Pattern.compile("[0-9]{2}\\/[A-Za-z]{3}\\/[0-9]{4}:\\d{2}:\\d{2}:\\d{2}");
		Pattern errorCodePattern = Pattern.compile("(\\t404|\\t408|\\/500)\\s");

		String errorCode = "";
		Matcher codeMatcher = errorCodePattern.matcher(input);
		if (codeMatcher.find()) {
			errorCode = codeMatcher.group(0).trim().replace("/", "");
		}

		String formatedDate = "";
		Matcher dateMatcher = datePattern.matcher(input);
		if (dateMatcher.find()) {
			String dateString = dateMatcher.group(0);

			DateTimeFormatter sdf =
					new DateTimeFormatterBuilder().parseCaseInsensitive()
							.appendPattern("dd/MMM/yyyy:HH:mm:ss")
							.toFormatter(Locale.ENGLISH);

			LocalDateTime date = LocalDateTime.parse(dateString, sdf);

			DateTimeFormatter fff = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			formatedDate = date.format(fff);
		}

		return formatedDate
				+ "|"
				+ errorCode
				+ "|"
				+ input;
	}

}
