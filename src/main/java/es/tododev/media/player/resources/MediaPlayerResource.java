package es.tododev.media.player.resources;


import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tododev.media.player.services.InvokeAndGet.SyncException;
import es.tododev.media.player.services.MediaPlayerService;

@RestController
@RequestMapping("/media/{app}")
public class MediaPlayerResource {
	
	private final Logger logger = LoggerFactory.getLogger(MediaPlayerResource.class);
	private final ApplicationContext applicationContext;
	
	@Inject
	public MediaPlayerResource(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
    
    @RequestMapping("/open")
    public ResponseEntity<String> open(@PathVariable("app") String app, @RequestParam(value="path", required=true) String path) throws Exception {
    	getApp(app).open(path);
    	return ResponseEntity.ok(path);
    }
    
    @RequestMapping("/play")
    public ResponseEntity<String> play(@PathVariable("app") String app) throws InterruptedException, SyncException {
    	getApp(app).play();
    	return ResponseEntity.ok("play");
    }
    
    @RequestMapping("/stop")
    public ResponseEntity<String> stop(@PathVariable("app") String app) throws Exception {
    	getApp(app).stop();
    	return ResponseEntity.ok("stop");
    }
    
    @RequestMapping("/pause")
    public ResponseEntity<String> pause(@PathVariable("app") String app) throws InterruptedException, SyncException {
    	getApp(app).pause();
    	return ResponseEntity.ok("pause");
    }
    
    @RequestMapping("/position")
    public ResponseEntity<String> open(@PathVariable("app") String app, @RequestParam(value="value", required=true) int value, @RequestParam(value="top", required=true) int top) throws InterruptedException, SyncException {
    	getApp(app).position(value, top);
    	return ResponseEntity.ok("Position "+value+"/"+top);
    }
    
    @RequestMapping("/close")
    public ResponseEntity<String> close(@PathVariable("app") String app) throws Exception {
    	getApp(app).close();
    	return ResponseEntity.ok("close");
    }
    
    @RequestMapping("/isrunning")
    public ResponseEntity<Boolean> isRunning(@PathVariable("app") String app) {
    	boolean started = getApp(app).isStarted();
    	return ResponseEntity.ok(started);
    }
    
    private MediaPlayerService getApp(String app) {
    	return applicationContext.getBean(app, MediaPlayerService.class);
    }
	
}
