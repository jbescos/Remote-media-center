package es.tododev.media.vlc.services;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.media.vlc.services.InvokeAndGet.SyncException;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

@Named
@Singleton
public class VlcPlayerService {

	private final Logger logger = LoggerFactory.getLogger(VlcPlayerService.class);
	private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private boolean started;
    
    public synchronized void open(String path) throws InterruptedException, SyncException {
    	close();
    	if(!started) {
        	started = new NativeDiscovery().discover();
        	if(!started) {
        		throw new IllegalStateException("Cannot find the VLC native libraries");
        	}else {
        		logger.debug("libvlc version {}", LibVlc.INSTANCE.libvlc_get_version());
        	}
    	}
    	InvokeAndGet.execute(() -> reload(path), 20000);
    }
    
    public synchronized void close() throws InterruptedException, SyncException {
    	if(frame != null) {
    		InvokeAndGet.execute(() -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)), 20000);
    	}
    	started = false;
    }
    
    public synchronized void pause() {
    	if(started) {
    		mediaPlayerComponent.getMediaPlayer().pause();
    	}
    }
    
    public synchronized void play() {
    	if(started) {
    		mediaPlayerComponent.getMediaPlayer().play();
    	}
    }
    
    public synchronized void position(int value, int top) {
    	if(started) {
    		float position = (float) value/top;
    		mediaPlayerComponent.getMediaPlayer().setPosition(position);
    	}
    }
    
    public synchronized void stop() {
    	if(started) {
    		mediaPlayerComponent.getMediaPlayer().stop();
    	}
    }
    
    private void reload(String path) {
    	logger.debug("Reload movie: {}", path);
    	mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    	frame = new JFrame(path);
    	frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    	frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().setPlaySubItems(true);
        mediaPlayerComponent.getMediaPlayer().playMedia(path);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
            }
        });
        started = true;
    }

	public synchronized boolean isStarted() {
		return started;
	}
    
}
