package ru.redwarlock.analyze;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainClass {

	public static void main(String[] args) {
		OperationCounter oc = new OperationCounter();
		//oc.analyzeNginx();
		oc.operateNginxResultFile();

	}
}
