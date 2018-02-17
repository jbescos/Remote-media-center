package es.tododev.media.player.services;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.media.common.process.ProcessManager;

@Named("omx")
@Singleton
public class OmxPlayerService implements MediaPlayerService{

	private final Logger logger = LoggerFactory.getLogger(OmxPlayerService.class);
	private final static String COMMAND = "omxplayer";
	private final ProcessManager processManager;
	
	@Inject
	public OmxPlayerService(ProcessManager processManager) {
		this.processManager = processManager;
	}
    
	@Override
    public synchronized void open(String path) throws IOException {
		processManager.prepare(COMMAND, path);
		processManager.start();
    }
    
	@Override
    public synchronized void close() {
		processManager.kill();
    }
    
	@Override
    public void pause() {}
    
	@Override
    public void play() {}
    
	@Override
    public void position(int value, int top) {}
    
	@Override
    public synchronized void stop() throws IOException {
		processManager.kill();
		processManager.start();
    }

    @Override
	public synchronized boolean isStarted() {
		return processManager.isAlive();
	}
    
}
