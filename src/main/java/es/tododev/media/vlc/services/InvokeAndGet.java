package es.tododev.media.vlc.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

public class InvokeAndGet {

	public static <T> T executeandGet(Supplier<T> supplier, long timeout) throws InterruptedException, SyncException {
		AtomicReference<T> atomicRef = new AtomicReference<>();
		AtomicReference<Exception> atomicException = new AtomicReference<>();
		CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(() -> {
			try {
				atomicRef.set(supplier.get());
			}catch(Exception e){
				atomicException.set(e);
			}finally {
				latch.countDown();
			}
		});
		latch.await(timeout, TimeUnit.MILLISECONDS);
		if(atomicException.get() != null) {
			throw new SyncException(atomicException.get());
		}else {
			return atomicRef.get();
		}
	}
	
	public static void execute(Runnable action, long timeout) throws InterruptedException, SyncException {
		executeandGet(() -> {
			action.run();
			return null;
		}, timeout);
	}
	
	@SuppressWarnings("serial")
	public static class SyncException extends Exception {
		public SyncException(Throwable arg0) {
			super(arg0);
		}
	}
	
}
