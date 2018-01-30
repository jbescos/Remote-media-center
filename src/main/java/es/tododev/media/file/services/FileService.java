package es.tododev.media.file.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class FileService {
	
	private final Logger logger = LoggerFactory.getLogger(FileService.class);

	public Stream<File> getFilesFromPath(String path) throws IOException{
		File head = Paths.get(path).toFile();
		if(head.exists()) {
			File[] children = head.listFiles();
			if(children != null) {
				Stream<File> files = Arrays.stream(children)
						.sorted(Comparator.comparing(file -> file.getName(), String.CASE_INSENSITIVE_ORDER));
				return files;
			}
		}
		return Stream.empty();
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
