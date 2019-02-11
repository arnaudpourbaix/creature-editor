import { Singleton } from "typescript-ioc";
import BaseReaderService from "./base-reader";
import { DynamicArray } from "./dynamic-array";
import { BaseResourceEntry, BiffResourceEntry } from "./ressource-entry";
import BiffEntry from "./biff-entry";
import fs = require('fs');
import _ = require('lodash');

@Singleton
export default class KeyfileService extends BaseReaderService {

    private resourceEntries: BaseResourceEntry[];
    private biffEntries: any[];
    private startIndex: number;
    
    static EXTENSIONS = [
        { key: 0x3ed, value: "ITM" },
        { key: 0x3ee, value: "SPL" },
        { key: 0x3ef, value: "BCS" },
        { key: 0x3f0, value: "IDS" },
        { key: 0x3f1, value: "CRE" },
        { key: 0x3f3, value: "DLG" },
        { key: 0x3f4, value: "2DA" },
        { key: 0x3f6, value: "STO" },
        { key: 0x3f8, value: "EFF" },
        { key: 0x3fd, value: "PRO" }
    ];

    constructor() { 
        super();
    }

    public init(): void {
        const file = 'D:/Games/Beamdog/00766/chitin.key';
        //const file = 'db-data/chitin.key';
        if (!fs.existsSync(file)) {
            throw "chitin.key not found on " + file;
        }
        this.buffer = fs.readFileSync(file);
        const signature = String(this.buffer.subarray(0, 4));
        const version = String(this.buffer.subarray(4, 8));
		if (signature.toUpperCase() !== "KEY ") {
			throw "invalid chitin.key file: " + file;
		}
		if (version.toUpperCase() !== "V1  ") {
			throw "invalid chitin.key version: " + file;
		}
        this.resourceEntries = [];
        this.biffEntries = [];
        this.readBiffEntries();
        this.readBiffResources();
    }

	public getResourceEntry(resourceName: string): BaseResourceEntry|undefined {
		return _.find(this.resourceEntries, (entry) => entry.resourceName === resourceName.toUpperCase());
    }
    
	public getResourceEntriesByExtension(extension: string) {
        extension = extension.toUpperCase();
        return _.filter(this.resourceEntries, (entry) => entry.extension === extension);
	}

    public addResourceEntry(entry: BaseResourceEntry): void {
        this.resourceEntries.push(entry);
    }

	private readBiffEntries(): void {
		let numbif = DynamicArray.getInt(this.buffer, 8);
		let bifoff = DynamicArray.getInt(this.buffer, 16);
		for (let i = 0; i < numbif; i++) {
			let entry = new BiffEntry(i, this.buffer, bifoff + 12 * i);
			this.biffEntries.push(entry);
        }
	}

	private readBiffResources(): void {
		let numres = DynamicArray.getInt(this.buffer, 12);
	    let resoff = DynamicArray.getInt(this.buffer, 20);
		for (let i = 0; i < numres; i++) {
			let entry = new BiffResourceEntry(this.buffer, resoff + 14 * i, 8);
			if (entry.extension !== null) {
				this.resourceEntries.push(entry);
			}
		}
	}

}