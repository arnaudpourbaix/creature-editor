class StringResource {
	private file: string;
	private charset = "utf8";

	public init(file: string) {
		this.file = file;
		//this.charset = Charset.forName("utf8");
	}

}
