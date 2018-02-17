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
	private final static String KILL_INPUT = "expect -c \"send \\003;\"";
	
	public void prepare(String ... command) throws IOException {
		if(command == null || command.length == 0) {
			throw new IllegalArgumentException("Illegal process arguments, must be at least 1");
		}
		StringBuilder builder = new StringBuilder();
		for(String c : command) {
			builder.append(c).append(" ");
		}
		processBuilder = new ProcessBuilder("bash", "-c", builder.toString());
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.to(createTemp("process_out_"+command[0])));
		processBuilder.redirectError(Redirect.to(createTemp("process_err_"+command[0])));
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
		}else {
			throw new IllegalArgumentException("The process is not running");
		}
	}
	
}
