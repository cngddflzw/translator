package org.uestc.translator.core;

import org.uestc.translator.AppContext;

public class DlHisWordsThread extends Thread {
	AppContext ac;
	
	public DlHisWordsThread(AppContext ac) {
		this.ac = ac;
	}
	
	@Override
	public void run() {
		RemoteDatabase.getHistory(ac);
	}
	
}
