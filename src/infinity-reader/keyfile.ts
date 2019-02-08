import { Inject, Singleton } from "typescript-ioc";
import fs = require('fs');

@Singleton
export default class KeyfileService {

    constructor() { }

    public async init() {
        //const file = 'D:/Games/Beamdog/00766/chitin.key';
        const file = 'db-data/chitin.key';
        if (!fs.existsSync(file)) {
            throw "chitin.key not found on " + file;
        }
        return fs.readFile(file, (err, buffer) => {
            const signature = String(buffer.subarray(0, 4));
            const version = String(buffer.subarray(4, 8));
        });
    }

}