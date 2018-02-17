package es.tododev.media.common.process;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class ProcessManager {

	private final Logger logger = LoggerFactory.getLogger(ProcessManager.class);
	private ProcessBuilder processBuilder;
	private Process process;
	
	public void prepare(String command) throws IOException {
		if(command == null) {
			throw new IllegalArgumentException("Illegal process arguments, must be at least 1");
		}
		processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		File processInOut = Files.createTempFile("process"+command, ".log").toFile();
		logger.info("Create output/input/error of the process {} in {}", command, processInOut.getAbsolutePath());
		processInOut.deleteOnExit();
		processBuilder.redirectOutput(Redirect.to(processInOut));
		processBuilder.redirectInput(Redirect.to(processInOut));
		processBuilder.redirectError(Redirect.to(processInOut));
	}
	
	public void start() throws IOException {
		if(isAlive()) {
			throw new IllegalArgumentException("The process is already running");
		}else {
			process = processBuilder.start();
		}
	}
	
	public boolean isAlive() {
		return process != null && process.isAlive();
	}
	
	public void kill() {
		if(isAlive()) {
			process.destroy();
		}else {
			throw new IllegalArgumentException("The process is not running");
		}
	}
	
}
