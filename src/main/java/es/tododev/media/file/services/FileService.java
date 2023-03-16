package es.tododev.media.file.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

@Named
@Singleton
public class FileService {
	
	private final Logger logger = LoggerFactory.getLogger(FileService.class);
	private final int FILEBUFFERSIZE = 102400000; // 100 MB

	public Stream<File> getFilesFromPath(String path) throws IOException{
		File head = loadFile(path);
		if(head.exists()) {
			File[] children = head.listFiles();
			if(children != null) {
				Stream<File> files = Arrays.stream(children)
						.filter(file -> !file.isHidden())
						.filter(file -> file.canRead())
						.sorted(Comparator.comparing(file -> file.getName(), String.CASE_INSENSITIVE_ORDER));
				return files;
			}
		}
		return Stream.empty();
	}
	
	private File loadFile(String path) {
		return Paths.get(path).toFile();
	}
	
	public void remove(String path) throws IOException {
		Path paths = Paths.get(path);
		Files.delete(paths);
		logger.info("{} removed", path);
	}
	
	public void streamFileOut(HttpServletResponse response, String path) throws IOException {
		Path filePath = Paths.get(path);
		File file = filePath.toFile();
    	response.setContentType(Files.probeContentType(filePath));
    	response.setContentLengthLong(file.length());
    	response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
    	byte[] bytes = new byte[FILEBUFFERSIZE];
    	int bytesRead;
    	OutputStream output = response.getOutputStream();
    	try(InputStream input = new FileInputStream(file)){
    		while ((bytesRead = input.read(bytes)) != -1) {
        		output.write(bytes, 0, bytesRead);
        		logger.debug("Reading {} bytes", bytesRead);
        	}
    	}
	}
	
	public void uploadFile(MultipartFile file, String rootLocation) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file " + filename);
        }
        logger.debug("Uploading file {}", filename);
        byte[] bytes = new byte[FILEBUFFERSIZE];
    	int bytesRead;
        File newFile = Paths.get(rootLocation, filename).toFile();
        InputStream input = file.getInputStream();
        try(OutputStream output = new FileOutputStream(newFile)){
        	while ((bytesRead = input.read(bytes)) != -1) {
        		output.write(bytes, 0, bytesRead);
        		output.flush();
        		logger.debug("Writting {} bytes", bytesRead);
        	}
        }
        logger.debug("Uploaded file in {}", newFile.getAbsolutePath());
    }
	
}
