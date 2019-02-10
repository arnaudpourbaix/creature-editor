import IInputStream from "./input-stream";

export default abstract class BaseReaderService {
    protected buffer: Buffer;
	protected startIndex: number;

    constructor() { }

    public readBytes(is: IInputStream, buffer: number[]): void {
        let bytesread = 0;
        while (bytesread < buffer.length) {
            let newread: number = (r => r.str.charCodeAt(r.cursor++))(is);
            if (newread === -1) {
                throw "Unable to read " + buffer.length + " bytes";
            }
            bytesread += newread;
        }
    }

    public readBytes2(is: IInputStream, length: number): number[] {
        let buffer: number[] = (s => { let a = []; while (s-- > 0) a.push(0); return a; })(length);
        let bytesread: number = 0;
        while (bytesread < length) {
            let newread: number = (r => r.str.charCodeAt(r.cursor++))(is);
            if (newread === -1) {
                throw "Unable to read " + length + " bytes";
            }
            bytesread += newread;
        }
        return buffer;
    }

    public readBytes3(is: IInputStream, buffer: number[], offset: number, length: number) {
        let bytesread: number = 0;
        while (bytesread < length) {
            let newread: number = (r => r.str.charCodeAt(r.cursor++))(is);
            if (newread === -1) {
                throw "Unable to read " + buffer.length + " bytes";
            }
            bytesread += newread;
        }
    }

    protected readInt(offset: number, length: number = 4): number {
        return this.getInt(this.buffer.subarray(offset, offset + length));
    }

    protected readString(offset: number, length: number): string {
        return String(this.buffer.subarray(offset, offset + length));
    }

    public getInt(buffer: Uint8Array, offset: number = 0): number {
        if (buffer != null && offset >= 0 && offset + 3 < buffer.length) {
            let v: number = buffer[offset] & 255;
            v |= (buffer[offset + 1] & 255) << 8;
            v |= (buffer[offset + 2] & 255) << 16;
            v |= (buffer[offset + 3] & 255) << 24;
            return v;
        } else return 0;
    }

    public getString(buffer: number[], offset: number, length: number): string {
        if (buffer != null && offset >= 0 && offset < buffer.length && length >= 0) {
            if (offset + length > buffer.length) {
                length = buffer.length - offset;
            } 
            for (let i: number = 0; i < length; i++) {
                {
                    if (buffer[offset + i] === 0) {
                        return String.fromCharCode.apply(null, buffer).substr(offset, i);
                    }
                };
            }
            return String.fromCharCode.apply(null, buffer).substr(offset, length);
        }
        return "";
    }
}