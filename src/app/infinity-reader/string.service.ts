import { Injectable } from "@angular/core";
import * as fs from 'fs';
import { Config } from "./config";
import { DynamicArray } from "./dynamic-array";
import FileReader from "./file-reader";

@Injectable({
   providedIn: "root"
})
export default class StringService {
   private nbEntries: number;
   private buffer: Buffer;
   private startIndex: number;

   constructor() {}

   public init(): void {
      if (!fs.existsSync(Config.DIALOG_FILE)) {
         throw "dialog.tlk not found on " + Config.DIALOG_FILE;
      }
      this.buffer = fs.readFileSync(Config.DIALOG_FILE);
      const signature = String(this.buffer.subarray(0, 4));
      const version = String(this.buffer.subarray(4, 8));
      if (signature.toUpperCase() !== "TLK ") {
         throw "invalid dialog.tlk file: " + Config.DIALOG_FILE;
      }
      if (version.toUpperCase() !== "V1  ") {
         throw "invalid dialog.tlk version: " + Config.DIALOG_FILE;
      }
      this.nbEntries = FileReader.readInt(this.buffer, 0xa, 4);
      this.startIndex = FileReader.readInt(this.buffer, 0xe, 4);
   }

   public getStringRef(index: number): string | null {
      if (index == -1) {
         return null;
      }
      if (index >= this.nbEntries || index < 0) {
         throw "Invalid string index: " + index;
      }
      index *= 0x1a;
      let offset = FileReader.readInt(this.buffer, 0x12 + index + 0x12, 4);
      let length = FileReader.readInt(this.buffer, 0x12 + index + 0x16, 4);
      let result = DynamicArray.getString(this.buffer, this.startIndex + offset, length);
      return result;
   }
}
