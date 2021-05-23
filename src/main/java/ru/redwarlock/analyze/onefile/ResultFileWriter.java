package ru.redwarlock.analyze.onefile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultFileWriter implements Runnable {

	private final BlockingQueue<String> findedLines;

	public ResultFileWriter(BlockingQueue<String> findedLines) {
		this.findedLines = findedLines;
	}

	@Override
	public void run() {

		System.out.println("*****  WRITE START *******");
		try (FileWriter fw = new FileWriter("myfile3.txt", true)) {
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			String line = "";
			int counter = 0;
			//out.println("ДАТА|КОД ОШИБКИ|IP|URL|ТЕКСТ ОШИБКИ");
			out.println("КОД ОШИБКИ|URL");

			while ((line = findedLines.poll(5, TimeUnit.SECONDS)) != null) {
				counter++;

				out.println(line);

				if (counter == 1000) {
					System.out.println("1000 lines wrote");
					counter = 0;
				}
			}

			System.out.println(counter + " lines wrote");

			out.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("*****  WRITE END *******");
	}



}
