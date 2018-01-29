package es.tododev.media.file.dto;

public class FileInfoDto {

	private String fileName;
	private String absolutePath;
	private boolean directory;
	
	public FileInfoDto(String fileName, boolean directory, String absolutePath) {
		this.fileName = fileName;
		this.directory = directory;
		this.absolutePath = absolutePath;
	}
	
	public FileInfoDto() {
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isDirectory() {
		return directory;
	}
	public void setDirectory(boolean directory) {
		this.directory = directory;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
}
