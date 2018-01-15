package es.tododev.media.file.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class FileService {

	public Stream<File> getFilesFromPath(String path) throws IOException{
		File head = Paths.get(path).toFile();
		if(head.exists()) {
			Stream<File> files = Arrays.stream(head.listFiles()).sorted(Comparator.comparing(file -> file.getName(), String.CASE_INSENSITIVE_ORDER));
			return files;
		} else {
			throw new IllegalArgumentException("Invalid file path: "+path);
		}
	}
	
}
