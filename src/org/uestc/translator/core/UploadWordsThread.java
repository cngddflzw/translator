package org.uestc.translator.core;

import org.uestc.translator.AppContext;

public class UploadWordsThread extends Thread {
	AppContext ac;
	
	public UploadWordsThread(AppContext ac) {
		this.ac = ac;
	}
	
	@Override
	public void run() {
		RemoteDatabase.addHisNewWords(ac.getUsername(),
				ac.getHistorySet(), ac.getNewWordSet());
	}
	
}
