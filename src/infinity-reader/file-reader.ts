import { DynamicArray } from "./dynamic-array";

export default class FileReader {

    private constructor() { }

    static readInt(buffer: Buffer, offset: number, length: number = 4): number {
        return DynamicArray.getInt(buffer.subarray(offset, offset + length));
    }

}