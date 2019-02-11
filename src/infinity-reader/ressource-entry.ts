import Decryptor from "./decryptor";
import { DynamicArray } from "./dynamic-array";
import KeyfileService from "./keyfile-service";

export abstract class BaseResourceEntry {
	public resourceName: string;
	public extension: string;
    public overrideFile: string; // File
    
    constructor() {}

	public equals(entry: BaseResourceEntry): boolean {
		return this.resourceName.toUpperCase() === entry.resourceName.toUpperCase();
	}

	public abstract getResourceData(): any;

	public getResourceTextData(): string {
		let data = this.getResourceData();
		let text: string;
		if (data != null && data.length > 1 && data[0] === -1) {
			text = Decryptor.decrypt(data, 2, data.length);
		} else {
			text = new String(data).toString();
		}
		return text;
	}
}

export class FileResourceEntry extends BaseResourceEntry {
	private file: string; // File

	constructor(file: string) {
        super();
		this.file = file;
        this.resourceName = file.toUpperCase();
        this.extension = file.toUpperCase().slice(-3);
	}

	public getResourceData(): any {
		// InputStream is = new BufferedInputStream(new FileInputStream(file));
		// byte buffer[] = Filereader.readBytes(is, (int) file.length());
		// is.close();
		// return buffer;
    }
    
}

export class BiffResourceEntry extends BaseResourceEntry {
    overrideFile: string;
	type: number;
	locator: number;

	constructor (buffer: Buffer, offset: number, stringLength: number) {
        super();
        let sb = DynamicArray.getString(buffer, offset, stringLength);
		this.type = DynamicArray.getShort(buffer, offset + stringLength);
        this.locator = DynamicArray.getInt(buffer, offset + stringLength + 2);
        let ext = KeyfileService.EXTENSIONS.find((ext) => ext.key === this.type);
		if (!!ext) {
            this.extension = ext.value;
			sb += `.${this.extension}`;
		}
        this.resourceName = sb;
	}


	public getResourceData(): any {
    }

}
