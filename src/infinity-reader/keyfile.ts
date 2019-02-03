import { Inject, Singleton } from "typescript-ioc";
import fs = require('fs');

@Singleton
export default class KeyfileService {

    constructor() { }

    public init() {
        const file = fs.readFile('D:\\Games\\Beamdog\\00766\\chitin.key', (err, buffer) => {
            const signature = String(buffer.subarray(0, 4));
            const version = String(buffer.subarray(4, 8));
        });
    }

}