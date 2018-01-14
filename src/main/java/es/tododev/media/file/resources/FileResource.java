package es.tododev.media.file.resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tododev.media.file.dto.FileInfoDto;
import es.tododev.media.file.services.FileService;

@RestController
@RequestMapping("/files")
public class FileResource {

	private final FileService fileservice;
	
	@Inject
	public FileResource(FileService fileservice) {
		this.fileservice = fileservice;
	}
    
    @RequestMapping("/from")
    public ResponseEntity<List<FileInfoDto>> getFiles(@RequestParam(value="path", required=true) String path) throws IOException {
    	List<FileInfoDto> files = fileservice.getFilesFromPath(path).map(file -> new FileInfoDto(file.getName(), file.isDirectory())).collect(Collectors.toList());
    	return ResponseEntity.ok(files);
    }
	
}
