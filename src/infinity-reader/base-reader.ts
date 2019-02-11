import { DynamicArray } from "./dynamic-array";

export default abstract class BaseReaderService {
    protected buffer: Buffer;

    constructor() { }

    protected readInt(offset: number, length: number = 4): number {
        return DynamicArray.getInt(this.buffer.subarray(offset, offset + length));
    }

    protected readString(offset: number, length: number): string {
        return String(this.buffer.subarray(offset, offset + length));
    }

}