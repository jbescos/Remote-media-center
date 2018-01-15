package es.tododev.media.vlc.services;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

@Named
@Singleton
public class VlcPlayerService {

	private final static Logger log = LogManager.getLogger();
	private static final String NATIVE_LIBRARY_SEARCH_KEY = "vlc.lib";
	private static final String NATIVE_LIBRARY_SEARCH_PATH = System.getProperty(NATIVE_LIBRARY_SEARCH_KEY);
	private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private boolean started;
    
    public synchronized void open(String path) {
    	if(!started) {
        	started = new NativeDiscovery().discover();
        	if(!started) {
        		log.info("Trying to find the vlclib by "+NATIVE_LIBRARY_SEARCH_KEY+"="+NATIVE_LIBRARY_SEARCH_PATH);
        		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        		log.debug("LibVlc version {}", LibVlc.INSTANCE.libvlc_get_version());
        	}
    	}
    	close();
    	SwingUtilities.invokeLater(() -> reload(path));
    	
    }
    
    public synchronized void close() {
    	if(frame != null) {
    		SwingUtilities.invokeLater(() -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
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
    
    public synchronized void skip(long delta) {
    	if(started) {
    		mediaPlayerComponent.getMediaPlayer().skip(delta);
    	}
    }
    
    public synchronized void stop() {
    	if(started) {
    		mediaPlayerComponent.getMediaPlayer().stop();
    	}
    }
    
    private void reload(String path) {
    	mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    	frame = new JFrame(path);
    	frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    	frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(false);
        mediaPlayerComponent.getMediaPlayer().playMedia(path);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
            }
        });
        started = true;
    }
	
}
