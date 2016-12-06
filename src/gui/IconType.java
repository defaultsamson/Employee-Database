package gui;

public enum IconType {
	ADD("ic_add_black_48dp_1x.png"),
	REMOVE("ic_remove_black_48dp_1x.png"),
	EDIT("ic_edit_black_48dp_1x.png"),
	DONE("ic_done_black_48dp_1x.png"),
	SEARCH("ic_search_black_18dp_1x.png"),
	SAVE("ic_save_black_48dp_1x.png"),
	USA("usa.jpg");

	private String textureDir;

	IconType(String textureDir) {
		this.textureDir = textureDir;
	}

	public String getTextureDir() {
		return textureDir;
	}
}