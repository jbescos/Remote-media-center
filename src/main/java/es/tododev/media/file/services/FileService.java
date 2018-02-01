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
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Named
@Singleton
public class FileService {
	
	private final Logger logger = LoggerFactory.getLogger(FileService.class);
	private final int FILEBUFFERSIZE = 1024; 

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
        	}
    	}
	}
	
	public void uploadFile(MultipartFile file, Path rootLocation) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file " + filename);
        }
        Files.copy(file.getInputStream(), rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
    }
	
	public File loadFileFromClasspath(String path) throws IOException {
		String filename = path.substring(path.lastIndexOf("/"));
		logger.debug("Filename {}", filename);
		String[] parsed = filename.split("\\.");
		File temp = File.createTempFile(parsed[0], "."+parsed[1]);
		temp.deleteOnExit();
		try(InputStream is = FileService.class.getResourceAsStream(path); FileOutputStream fos = new FileOutputStream(temp)){
			int count;
			byte[] buf = new byte[1024];
			while ((count = is.read(buf, 0, buf.length)) > 0) {
				fos.write(buf, 0, count);
			}
		}
		return temp;
	}
	
}
