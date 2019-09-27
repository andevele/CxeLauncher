package com.cxel.launcher.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.util.Log;

/**
 * Executor that runs a task on a new background thread.
 */
public class DiskIOThreadExecutor implements Executor {

	private final Executor mDiskIO;

	public DiskIOThreadExecutor() {
		mDiskIO = Executors.newSingleThreadExecutor();
	}

	@Override
	public void execute(Runnable command) {
		if (command == null) {
			try {
				Log.e("zhulf", "comman is null,please check code");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		mDiskIO.execute(command);
	}
}
