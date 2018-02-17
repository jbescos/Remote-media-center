package es.tododev.media.player.services;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
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
	private final Robot robot;
	
	@Inject
	public OmxPlayerService(ProcessManager processManager) throws AWTException {
		this.processManager = processManager;
		robot = new Robot();
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
    public synchronized void pause() {
		robot.keyRelease(KeyEvent.VK_SPACE);
    }
    
	@Override
    public synchronized void play() {
		robot.keyRelease(KeyEvent.VK_SPACE);
    }
    
	@Override
    public synchronized void position(int value, int top) {
		int key = value > 0 ? KeyEvent.VK_RIGHT : KeyEvent.VK_LEFT;
		robot.keyRelease(key);
    }
    
	@Override
    public synchronized void stop() throws IOException {
		processManager.kill();
		processManager.start();
		robot.keyRelease(KeyEvent.VK_SPACE);
    }

    @Override
	public synchronized boolean isStarted() {
		return processManager.isAlive();
	}
    
}
