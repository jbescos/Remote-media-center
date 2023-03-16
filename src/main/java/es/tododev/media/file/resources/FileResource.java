package es.tododev.media.file.resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.tododev.media.file.dto.FileInfoDto;
import es.tododev.media.file.services.FileService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/files")
public class FileResource {

	private final Logger logger = LoggerFactory.getLogger(FileResource.class);
	private final FileService fileservice;
	
	@Inject
	public FileResource(FileService fileservice) {
		this.fileservice = fileservice;
	}
    
    @RequestMapping("/from")
    public ResponseEntity<List<FileInfoDto>> getFiles(@RequestParam(value="path", required=true) String path) throws IOException {
    	List<FileInfoDto> files = fileservice.getFilesFromPath(path).map(file -> new FileInfoDto(file.getName(), file.isDirectory(), file.getAbsolutePath())).collect(Collectors.toList());
    	return ResponseEntity.ok(files);
    }
    
    @RequestMapping("/download")
    public void download(@RequestParam(value="path", required=true) String path, HttpServletResponse response) throws IOException {
    	fileservice.streamFileOut(response, path);
    }
    
    @RequestMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam(value="path", required=true) String path) throws IOException {
    	fileservice.remove(path);
    	return ResponseEntity.ok(path+" removed");
    }
    
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException {
    	fileservice.uploadFile(file, path);
        return "Uploaded "+file.getOriginalFilename();
    }
	
}
