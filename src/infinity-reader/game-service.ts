import { Inject, Singleton } from "typescript-ioc";
import fs = require('fs');
import path = require('path');
import KeyfileService from "./keyfile-service";
import StringService from "./string-service";
import { ResourceEntry, BiffResourceEntry } from "./ressource-entry";

@Singleton
export default class GameService {


	constructor(@Inject private keyFileService: KeyfileService, @Inject private stringService: StringService) { }

	public async openGame(): Promise<void> {
		this.keyFileService.init();
		this.stringService.init();
		// Add override resources
		console.log("GameService", "Adding override resources");
		const folder = 'D:/Games/Beamdog/00766/Override';
		if (fs.existsSync(folder)) {
			fs.readdirSync(folder).forEach((file) => {
				let entry = this.keyFileService.getResourceEntry(file);
				if (!entry) {
					this.keyFileService.addResourceEntry(new ResourceEntry(file));
				} else if (entry instanceof BiffResourceEntry) {
					entry.overrideFile = file;
				}
			});
		}
		let cres = this.keyFileService.getResourceEntriesByExtension("cre");
		console.log(cres);
	}

}