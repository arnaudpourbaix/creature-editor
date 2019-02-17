import { Config } from "./config";
import { Injectable } from '@angular/core';
import KeyfileService from "./keyfile.service";
import { BiffResourceEntry, FileResourceEntry } from "./ressource-entry";
import StringService from "./string.service";
import * as fs from 'fs';

@Injectable({
   providedIn: "root"
})
export default class GameService {
    constructor(private keyFileService: KeyfileService, private stringService: StringService) {}

    public async openGame(): Promise<void> {
        this.keyFileService.init();
        this.stringService.init();
        // Add override resources
        if (fs.existsSync(Config.OVERRIDE_FOLDER)) {
            fs.readdirSync(Config.OVERRIDE_FOLDER).forEach(file => {
                let entry = this.keyFileService.getResourceEntry(file);
                if (!entry) {
                    this.keyFileService.addResourceEntry(new FileResourceEntry(file));
                } else if (entry instanceof BiffResourceEntry) {
                    entry.overrideFile = file;
                }
            });
        }
    }
}
