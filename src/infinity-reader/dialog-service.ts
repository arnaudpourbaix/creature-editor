import { Inject, Singleton } from "typescript-ioc";

@Singleton
export default class StringResource {
	private file: string;
	private charset = "utf8";

	constructor() { }

	public init(file: string) {
		this.file = file;
		//this.charset = Charset.forName("utf8");
	}

}
