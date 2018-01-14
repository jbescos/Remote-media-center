package es.tododev.media.file.dto;

public class FileInfoDto {

	private String fileName;
	private boolean directory;
	
	public FileInfoDto(String fileName, boolean directory) {
		this.fileName = fileName;
		this.directory = directory;
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
	
}
