package es.tododev.media.vlc.services;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.sun.jna.NativeLibrary;

import es.tododev.media.file.services.FileService;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

@Named
@Singleton
public class VlcPlayerService {

	private final static Logger log = LogManager.getLogger();
	private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private boolean started;
    private final static String OS = System.getProperty("os.name").toLowerCase();
    private final static String LINUX_SUBFIX = ".so";
    private final static String WINDOWS_SUBFIX = ".dll";
    private final static String[] LIBS = new String[] {"libvlccore", "libvlc"};
    private final FileService fileService;
    
    @Inject
    public VlcPlayerService(FileService fileService) {
    	this.fileService = fileService;
    }
    
    public synchronized void open(String path) throws IOException {
    	if(!started) {
    		String subfix = null;
    		if(isWindows()) {
    			subfix = WINDOWS_SUBFIX;
    		}else if(isUnix()) {
    			subfix = LINUX_SUBFIX;
    		}else {
    			throw new IllegalArgumentException(OS+" is not supported");
    		}
    		for(String lib : LIBS) {
    			String filePath = fileService.loadFileFromClasspath("/lib/win64/"+lib+subfix).getAbsolutePath();
    			log.info("Loading lib {}", filePath);
    			System.load(filePath);
    		}
        	started = new NativeDiscovery().discover();
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
    
    private boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
	
    private boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}
}
