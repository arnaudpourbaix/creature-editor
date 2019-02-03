import fs = require('fs');

class KeyfileService {

    public init() {
        const file = fs.readFile('D:\\Games\\Beamdog\\00766\\chitin.key', (err, buffer) => {
            const signature = String(buffer.subarray(0, 4));
            const version = String(buffer.subarray(4, 8));
        });
    }

}

export var service = new KeyfileService();
