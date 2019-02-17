import { DynamicArray } from "./dynamic-array";

export default class BiffEntry {
   filename: string;
   location: number;
   index: number;
   filelength: number;
   stringoffset: number;
   stringlength: number;

   // constructor(filename: string) {
   // 	// Location: Indicates where file might be found
   // 	// Bit 1: Data or movies (LSB)
   // 	// Bit 2: ???
   // 	// Bit 3: ??? (CD1-directory?)
   // 	// Bit 4: CD2-directory
   // 	// Bit 5: CD3-directory
   // 	// Bit 6: CD4-directory
   // 	// Bit 7: CD5-directory
   // 	// Bit 8: ??? (Doesn't exist?) (MSB)
   //  this.filename = filename;
   // 	this.location = 1; // Data or movies
   // 	this.index = -1; // Not put into keyfile yet
   // }

   //    constructor(index: number, buffer: Buffer, offset: number, usesShortFormat: boolean) {
   //       this.index = index;
   //       this.stringoffset = DynamicArray.getInt(buffer, offset);
   //       this.stringlength = DynamicArray.getShort(buffer, offset + 4);
   //       this.location = DynamicArray.getShort(buffer, offset + 6);
   //       this.filename = DynamicArray.getString(buffer, this.stringoffset, this.stringlength);
   //       if (this.filename.startsWith("\\")) {
   //          this.filename = this.filename.substring(1);
   //       }
   //       this.filename = this.filename.replace("\\", "/").replace(":", "/");
   //    }

   constructor(index: number, buffer: Buffer, offset: number) {
      this.index = index;
      this.filelength = DynamicArray.getInt(buffer, offset);
      this.stringoffset = DynamicArray.getInt(buffer, offset + 4);
      this.stringlength = DynamicArray.getShort(buffer, offset + 8);
      this.location = DynamicArray.getShort(buffer, offset + 10);
      this.filename = DynamicArray.getString(buffer, this.stringoffset, this.stringlength);
      if (this.filename.startsWith("\\")) {
         this.filename = this.filename.substring(1);
      }
      this.filename = this.filename.replace("\\", "/").replace(":", "/");
   }

   public toString() {
      return this.filename;
   }
}
