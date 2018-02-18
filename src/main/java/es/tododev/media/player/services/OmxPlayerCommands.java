package es.tododev.media.player.services;

public enum OmxPlayerCommands {

	RTSP("rtsp:") {
		@Override
		public String getCommand(String input) {
			return "omxplayer "+input;
		}
	},
	FILE("file:") {
		@Override
		public String getCommand(String input) {
			return "omxplayer "+input;
		}
	};
	
	private final String protocol;
	
	private OmxPlayerCommands(String protocol) {
		this.protocol = protocol;
	}
	
	public abstract String getCommand(String input);
	
	public static OmxPlayerCommands getOmxPlayerCommands(String input) {
		if(input != null) {
			String inputLower = input.toLowerCase();
			for(OmxPlayerCommands command : OmxPlayerCommands.values()) {
				if(inputLower.startsWith(command.protocol)) {
					return command;
				}
			}
			return FILE;
		}else {
			throw new NullPointerException("Invalid input");
		}
	}
	
}
