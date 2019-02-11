import { Singleton } from "typescript-ioc";
import fs = require('fs');
import BaseReaderService from "./base-reader";

@Singleton
export default class StringService extends BaseReaderService {
	private nbEntries: number;
	private startIndex: number;

	constructor() { 
		super();
	}

	public init(): void {
		const file = 'D:/Games/Beamdog/00766/lang/en_US/dialog.tlk';
        if (!fs.existsSync(file)) {
            throw "dialog.tlk not found on " + file;
        }
        this.buffer = fs.readFileSync(file);
		const signature = String(this.buffer.subarray(0, 4));
		const version = String(this.buffer.subarray(4, 8));
		if (signature.toUpperCase() !== "TLK ") {
			throw "invalid dialog.tlk file: " + file;
		}
		if (version.toUpperCase() !== "V1  ") {
			throw "invalid dialog.tlk version: " + file;
		}
		this.nbEntries = this.readInt(0xA, 4);
		this.startIndex = this.readInt(0xE, 4);
	}

	public getStringRef(index: number): string|null {
		if (index == -1) {
			return null;
		}
		if (index >= this.nbEntries || index < 0) {
			throw "Invalid string index: " + index;
		}
		index *= 0x1A;
		let offset = this.readInt(0x12 + index + 0x12, 4);
		let length = this.readInt(0x12 + index + 0x16, 4);
		let result = this.readString(this.startIndex + offset, length);
		return result;
	}

}
