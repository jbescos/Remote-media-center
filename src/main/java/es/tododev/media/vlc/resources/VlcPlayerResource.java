package es.tododev.media.vlc.resources;


import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tododev.media.vlc.services.InvokeAndGet.SyncException;
import es.tododev.media.vlc.services.VlcPlayerService;

@RestController
@RequestMapping("/vlc")
public class VlcPlayerResource {

	private final VlcPlayerService vlcPlayerService;
	
	@Inject
	public VlcPlayerResource(VlcPlayerService vlcPlayerService) {
		this.vlcPlayerService = vlcPlayerService;
	}
    
    @RequestMapping("/open")
    public ResponseEntity<String> open(@RequestParam(value="path", required=true) String path) throws InterruptedException, SyncException {
    	vlcPlayerService.open(path);
    	return ResponseEntity.ok(path);
    }
    
    @RequestMapping("/play")
    public ResponseEntity<String> play() throws InterruptedException, SyncException {
    	vlcPlayerService.play();
    	return ResponseEntity.ok("play");
    }
    
    @RequestMapping("/stop")
    public ResponseEntity<String> stop() throws InterruptedException, SyncException {
    	vlcPlayerService.stop();
    	return ResponseEntity.ok("stop");
    }
    
    @RequestMapping("/pause")
    public ResponseEntity<String> pause() throws InterruptedException, SyncException {
    	vlcPlayerService.pause();
    	return ResponseEntity.ok("pause");
    }
    
    @RequestMapping("/position")
    public ResponseEntity<String> open(@RequestParam(value="value", required=true) int value, @RequestParam(value="top", required=true) int top) throws InterruptedException, SyncException {
    	vlcPlayerService.position(value, top);
    	return ResponseEntity.ok("Position "+value+"/"+top);
    }
    
    @RequestMapping("/close")
    public ResponseEntity<String> close() throws InterruptedException, SyncException {
    	vlcPlayerService.close();
    	return ResponseEntity.ok("close");
    }
    
    @RequestMapping("/isrunning")
    public ResponseEntity<Boolean> isRunning() {
    	boolean started = vlcPlayerService.isStarted();
    	return ResponseEntity.ok(started);
    }
	
}
