package ru.redwarlock.analyze;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import ru.redwarlock.analyze.nginx.NginxFileProcessor;
import ru.redwarlock.analyze.nginx.NginxFileReader;
import ru.redwarlock.analyze.nginx.NginxFileWriter;
import ru.redwarlock.analyze.onefile.ResultFileProcessor;
import ru.redwarlock.analyze.onefile.ResultFileReader;
import ru.redwarlock.analyze.onefile.ResultFileWriter;

public class BigFileWholeProcessor {

	private static final int NUMBER_OF_THREADS = 10;


	public void processFile(List<File> files) {
		BlockingQueue<String> fileContent = new LinkedBlockingQueue<String>(1000000);
		BlockingQueue<String> lines = new LinkedBlockingQueue<String>();

		NginxFileReader bigFileReader = new NginxFileReader(files, fileContent);
		NginxFileProcessor bigFileProcessor = new NginxFileProcessor(fileContent, lines);
		NginxFileWriter bigFileWriter = new NginxFileWriter(lines);

		ExecutorService es = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		es.execute(bigFileReader);
		es.execute(bigFileProcessor);
		es.execute(bigFileWriter);
		es.shutdown();
	}

	public void processFile(File file) {
		BlockingQueue<String> fileContent = new LinkedBlockingQueue<String>(1000000);
		BlockingQueue<String> lines = new LinkedBlockingQueue<String>();

		ResultFileReader resultFileReader = new ResultFileReader(file, fileContent);
		ResultFileProcessor resultFileProcessor = new ResultFileProcessor(fileContent, lines);
		ResultFileWriter resultFileWriter = new ResultFileWriter(lines);

		ExecutorService es = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		es.execute(resultFileReader);
		es.execute(resultFileProcessor);
		es.execute(resultFileWriter);
		es.shutdown();
	}


}
