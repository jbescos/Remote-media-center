package es.tododev.media.vlc.resources;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> open(@RequestParam(value="path", required=true) String path) throws IOException {
    	vlcPlayerService.open(path);
    	return ResponseEntity.ok(path);
    }
    
    @RequestMapping("/play")
    public ResponseEntity<String> play() {
    	vlcPlayerService.play();
    	return ResponseEntity.ok("play");
    }
    
    @RequestMapping("/stop")
    public ResponseEntity<String> stop() {
    	vlcPlayerService.stop();
    	return ResponseEntity.ok("stop");
    }
    
    @RequestMapping("/pause")
    public ResponseEntity<String> pause() {
    	vlcPlayerService.pause();
    	return ResponseEntity.ok("pause");
    }
    
    @RequestMapping("/skip")
    public ResponseEntity<String> open(@RequestParam(value="delta", required=true) long delta) {
    	vlcPlayerService.skip(delta);
    	return ResponseEntity.ok("Skip "+delta);
    }
    
    @RequestMapping("/close")
    public ResponseEntity<String> close() {
    	vlcPlayerService.close();
    	return ResponseEntity.ok("close");
    }
	
}
