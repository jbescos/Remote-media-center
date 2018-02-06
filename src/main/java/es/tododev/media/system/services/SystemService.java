package es.tododev.media.system.services;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SystemService {

	public void shutdown() throws IOException {
		OS.getOS().shutdown();
	}
	
	public void restart() throws IOException {
		OS.getOS().restart();
	}
	
	private static enum OS {
		Linux("Linux"){
			@Override
			public void shutdown() throws IOException {
				Runtime.getRuntime().exec("shutdown -h now");
				System.exit(0);
			}
			@Override
			public void restart() throws IOException {
				Runtime.getRuntime().exec("reboot");
				System.exit(0);
			}
		},
		Windows("Win"){
			@Override
			public void shutdown() throws IOException {
				Runtime.getRuntime().exec("shutdown.exe -s -t 0");
				System.exit(0);
			}
			@Override
			public void restart() throws IOException {
				Runtime.getRuntime().exec("shutdown.exe -r -t 0");
				System.exit(0);
			}
		},
		MacOS("Mac"){
			@Override
			public void shutdown() throws IOException {
				Runtime.getRuntime().exec("shutdown -h now");
				System.exit(0);
			}
			@Override
			public void restart() throws IOException {
				Runtime.getRuntime().exec("reboot");
				System.exit(0);
			}
		},
		Unknown("Unknown");
		
		private final String osName;
		
		private OS(String osName) {
			this.osName = osName;
		}
		
		public void shutdown() throws IOException {
			throw new RuntimeException("Unsupported operating system.");
		}
		public void restart() throws IOException {
			throw new RuntimeException("Unsupported operating system.");
		}
		public static OS getOS() {
			String operatingSystem = System.getProperty("os.name");
			for(OS os : OS.values()) {
				if(operatingSystem.startsWith(os.osName)) {
					return os;
				}
			}
			return Unknown;
		}
		
	}
	
}
