package es.tododev.media.system.resources;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.tododev.media.system.services.SystemService;

@RestController
@RequestMapping("/system")
public class SystemResource {
	
	private final SystemService systemService;
	
	public SystemResource(SystemService systemService) {
		this.systemService = systemService;
	}

	@RequestMapping(path = "/restart", method = RequestMethod.GET)
	public ResponseEntity<String> restart() throws IOException {
		systemService.restart();
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(path = "/shutdown", method = RequestMethod.GET)
	public ResponseEntity<String> shutdown() throws IOException {
		systemService.shutdown();
		return ResponseEntity.ok().build();
	}
	
}
