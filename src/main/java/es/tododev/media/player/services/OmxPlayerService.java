package es.tododev.media.player.services;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.media.common.process.ProcessManager;

@Named("omx")
@Singleton
public class OmxPlayerService implements MediaPlayerService{

	private final Logger logger = LoggerFactory.getLogger(OmxPlayerService.class);
	private final ProcessManager processManager;
	
	@Inject
	public OmxPlayerService(ProcessManager processManager) {
		this.processManager = processManager;
	}
    
	@Override
    public synchronized void open(String path) throws IOException {
		processManager.prepare("omxplayer "+path);
		processManager.start();
    }
    
	@Override
    public synchronized void close() throws IOException {
		processManager.kill();
    }
    
	@Override
    public void pause() {}
    
	@Override
    public void play() {}
    
	@Override
    public void position(int value, int top) {}
    
	@Override
    public synchronized void stop() throws IOException {}

    @Override
	public synchronized boolean isStarted() {
		return processManager.isAlive();
	}
    
}
