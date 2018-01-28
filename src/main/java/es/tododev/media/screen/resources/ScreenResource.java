package es.tododev.media.screen.resources;

import java.awt.AWTException;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tododev.media.screen.services.ScreenService;

@RestController
@RequestMapping("/screen")
public class ScreenResource {
	
	private final ScreenService screenService;
	
	public ScreenResource(ScreenService screenService) {
		this.screenService = screenService;
	}

	@RequestMapping(path = "/shoot", method = RequestMethod.GET)
	public ResponseEntity<Resource> shoot(@RequestParam(value="width", required=true) int widthScale, @RequestParam(value="height", required=true) int heightScale) throws IOException, AWTException {
		byte[] screenshoot = screenService.screenShoot(widthScale, heightScale);
		ByteArrayResource resource = new ByteArrayResource(screenshoot);
		HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=screenshoot.jpg");
	    header.set(HttpHeaders.CONTENT_LENGTH, screenshoot.length+"");
		return ResponseEntity.ok().headers(header).body(resource);
	}

}
