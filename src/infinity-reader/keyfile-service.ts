import { Inject, Singleton } from "typescript-ioc";
import fs = require('fs');
import _ = require('lodash');
import { ResourceEntry } from "./ressource-entry";
import BiffEntry from "./biff-entry";
import BaseReaderService from "./base-reader";

@Singleton
export default class KeyfileService extends BaseReaderService {

    private resourceEntries: ResourceEntry[];
    private biffEntries: BiffEntry[];

    constructor() { 
        super();
    }

    public init(): void {
        const file = 'D:/Games/Beamdog/00766/chitin.key';
        //const file = 'db-data/chitin.key';
        if (!fs.existsSync(file)) {
            throw "chitin.key not found on " + file;
        }
        let buffer = fs.readFileSync(file);
        const signature = String(buffer.subarray(0, 4));
        const version = String(buffer.subarray(4, 8));
        this.resourceEntries = [];
        this.biffEntries = [];
        this.readBiffEntries();
        this.readBiffResources();
    }

	public getResourceEntry(resourceName: string): ResourceEntry|undefined {
		return _.find(this.resourceEntries, (entry) => entry.resourceName === resourceName.toUpperCase());
    }
    
	public getResourceEntriesByExtension(extension: string) {
        extension = extension.toUpperCase();
        return _.filter(this.resourceEntries, (entry) => entry.extension === extension);
	}

    public addResourceEntry(entry: ResourceEntry): void {
        this.resourceEntries.push(entry);
    }

	private readBiffEntries(): void {
		let numbif = this.getInt(this.buffer, 8);
		let bifoff = this.getInt(this.buffer, 16);
		let biffEntries: BiffEntry[] = [];
		for (let i = 0; i < numbif; i++) {
			// let entry = new BiffEntry(i, buffer, bifoff + 12 * i);
			// biffEntries.add(entry);
		}
	}

	private readBiffResources(): void {
		let numres = this.getInt(this.buffer, 12);
	    let resoff = this.getInt(this.buffer, 20);
		for (let i = 0; i < numres; i++) {
			// let entry = new BiffResourceEntry(buffer, resoff + 14 * i, 8);
			// if (entry.getExtension() != null) {
			// 	resourceEntries.add(entry);
			// }
		}
	}

}