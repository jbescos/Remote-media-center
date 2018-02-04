package es.tododev.media.screen.resources;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tododev.media.screen.dto.EventData;
import es.tododev.media.screen.services.ScreenService;

@RestController
@RequestMapping("/screen")
public class ScreenResource {
	
	private final Logger logger = LoggerFactory.getLogger(ScreenResource.class);
	private final ScreenService screenService;
	
	public ScreenResource(ScreenService screenService) {
		this.screenService = screenService;
	}

	@RequestMapping(path = "/shoot", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> shoot() throws IOException {
		byte[] screenshoot = screenService.screenShoot();
		ByteArrayResource resource = new ByteArrayResource(screenshoot);
		HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=screenshoot.jpg");
	    header.set(HttpHeaders.CONTENT_LENGTH, screenshoot.length+"");
		return ResponseEntity.ok().headers(header).body(resource);
	}
	
	@RequestMapping(path = "/action", method = RequestMethod.POST)
	public ResponseEntity<String> mouseMove(@RequestBody List<EventData> events) throws IOException {
		for(EventData event : events) {
			if(event.getAction() == EventData.MOUSE_MOVE) {
				screenService.mouseMove(event.getX(), event.getY(), event.getWidth(), event.getHeight());
			}else if(event.getAction() == EventData.MOUSE_CLICK) {
				screenService.mouseClick(event.getButton(), event.getEvent());
			}else if(event.getAction() == EventData.KEY_PRESS) {
				screenService.keyboardPress(event.getButton(), event.getEvent());
			}else {
				logger.warn("Unknown action {}", event.getAction());
			}
		}
		screenService.waitTillEventsDone();
		String encoded = screenService.screenShootBase64();
		return ResponseEntity.ok().body(encoded);
	}
	
	@RequestMapping(path = "/mouse/move", method = RequestMethod.GET)
	public ResponseEntity<String> mouseMove(@RequestParam(value="x", required=true) int x, @RequestParam(value="y", required=true) int y, 
			@RequestParam(value="width", required=true) int width, @RequestParam(value="height", required=true) int height) throws IOException {
		screenService.mouseMove(x, y, width, height);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(path = "/mouse/click", method = RequestMethod.GET)
	public ResponseEntity<String> mouseClick(@RequestParam(value="event", required=true) int event, @RequestParam(value="button", required=true) int button) throws IOException {
		screenService.mouseClick(button, event);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(path = "/keyboard", method = RequestMethod.GET)
	public ResponseEntity<String> keyboardPress(@RequestParam(value="event", required=true) int event, @RequestParam(value="key", required=true) int key) throws IOException {
		screenService.keyboardPress(key, event);
		return ResponseEntity.ok().build();
	}

}
