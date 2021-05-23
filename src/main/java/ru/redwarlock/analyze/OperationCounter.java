package ru.redwarlock.analyze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OperationCounter {

	public void analyze() {

		ArrayList<File> fileToAnalyze = new ArrayList();
//		fileToAnalyze.add(new File("Z:\\Logs\\save log\\light\\version-1.12.4-save/test.log"));
		fileToAnalyze.add(new File("Z:\\Logs\\save log\\light\\version-1.12.4-save/crshb-light-app-mortgage.log"));
		fileToAnalyze.add(new File("Z:\\Logs\\save log\\light\\version-1.12.4-save/crshb-light-app-mortgage.1.log"));
		fileToAnalyze.add(new File("Z:\\Logs\\save log\\light\\version-1.12.4-save/crshb-light-app-mortgage.2.log"));
		fileToAnalyze.add(new File("Z:\\Logs\\save log\\light\\version-1.12.4-save/crshb-light-app-mortgage.3.log"));

		HashMap<LocalDateTime, Long> countedLines = new HashMap<>();

		for (File file : fileToAnalyze) {
			analyzeFile(file, countedLines);
		}

		LinkedHashMap<LocalDateTime, Long> sortedLines = sortHashMap(countedLines);

		writeResultFile(sortedLines);
	}

	private void writeResultFile(LinkedHashMap<LocalDateTime, Long> countedLines) {
		File fout = new File("out2.txt");

		try (FileOutputStream fos = new FileOutputStream(fout)) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (Map.Entry<LocalDateTime, Long> set : countedLines.entrySet()) {
				bw.write(set.getKey().toString() + ": " + set.getValue());
				bw.newLine();
			}

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LinkedHashMap<LocalDateTime, Long> sortHashMap(HashMap<LocalDateTime, Long> countedLines) {
		//LinkedHashMap preserve the ordering of elements in which they are inserted
		LinkedHashMap<LocalDateTime, Long> reverseSortedMap = new LinkedHashMap<>();

//		countedLines.entrySet()
//				.stream()
//				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		countedLines.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		return reverseSortedMap;
	}

	private void analyzeFile(File file, HashMap<LocalDateTime, Long> countedLines) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			reader.read();
			reader.lines();

//			// считаем сначала первую строку
//			String line = reader.readLine();
//
//			while (line != null) {
//
//				analyzeLine(line, countedLines);
//
//				// считываем остальные строки в цикле
//				line = reader.readLine();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analyzeLine(String line, HashMap<LocalDateTime, Long> countedLines) {
		Pattern pattern = Pattern.compile("\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}");
		Matcher matcher = pattern.matcher(line);
		String dateString = null;
		if (matcher.find()) {
			dateString = matcher.group(0);
		}

		if (dateString != null) {
			DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime date = LocalDateTime.parse(dateString, sdf);

			if (countedLines.containsKey(date)) {
				countedLines.put(date, countedLines.get(date) + 1);
			} else {
				countedLines.put(date, 1L);
			}
		}
	}

	public void analyzeNginx() {
		File logDir = new File("C:\\Users\\RedWarlock\\Documents\\nginxLog\\accessLog");
		File[] fileDirs = logDir.listFiles();
		List<File> files = Arrays.asList(fileDirs).stream()
				.map(dir -> dir.listFiles()[0])
				.collect(Collectors.toList());

		BigFileWholeProcessor bigFileWholeProcessor = new BigFileWholeProcessor();
		bigFileWholeProcessor.processFile(files);
	}

	public void operateNginxResultFile() {
		File fileToAnalyze = new File("C:\\Users\\RedWarlock\\IdeaProjects\\LogAnalizator\\myfile.txt");

		BigFileWholeProcessor bigFileWholeProcessor = new BigFileWholeProcessor();
		bigFileWholeProcessor.processFile(fileToAnalyze);


	}

}
