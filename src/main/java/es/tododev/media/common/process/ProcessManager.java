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
		logger.info("Execute: {}", command);
		processBuilder = new ProcessBuilder("bash", "-c", command);
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectError(Redirect.to(createTemp("processmanager_err")));
	}
	
	private File createTemp(String name) throws IOException {
		File tmp = Files.createTempFile(name, ".log").toFile();
		tmp.deleteOnExit();
		logger.info("Create temp file of the in {}", tmp.getAbsolutePath());
		return tmp;
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
	
	public void kill() throws IOException {
		if(isAlive()) {
			process.getOutputStream().write('q');
			process.getOutputStream().flush();
			process = null;
			processBuilder = null;
		}else {
			throw new IllegalArgumentException("The process is not running");
		}
	}
	
}
