package org.uestc.translator.core;

import org.uestc.translator.AppContext;

public class DlNewWordsThread extends Thread {
	AppContext ac;
	
	public DlNewWordsThread(AppContext ac) {
		this.ac = ac;
	}
	
	@Override
	public void run() {
		RemoteDatabase.getNewWords(ac);
	}
	
}
