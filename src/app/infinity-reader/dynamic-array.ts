export enum ElementType {
   BYTE,
   SHORT,
   INTEGER24,
   INTEGER,
   LONG
}

/**
 * An array object which mimics to a certain degree the type casting behavior of pointers from other programming languages. The byte order is initially set to
 * little endian. Features include:
 * <ul>
 * <li>Converting between integer types of different sizes</li>
 * <li>Setting and modifying a base offset</li>
 * <li>Reading/writing data in big endian or little endian byte order</li>
 * <li>Static methods for quick array/value conversions</li>
 * </ul>
 */
export class DynamicArray {
   /**
    * Reads a byte from the specified buffer
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset.
    * @return {number} Byte value from buffer or 0 on error.
    */
   public static getByte(buffer: Uint8Array, offset: number): number {
      if (buffer != null && offset >= 0 && offset < buffer.length) {
         return buffer[offset];
      } else return 0;
   }

   /**
    * Writes a byte into the specified buffer.
    *
    * @param {Array} buffer
    * The buffer to write the value to.
    * @param {number} offset
    * Buffer offset.
    * @param {number} value
    * Value to write.
    * @return {boolean} true if successfull, false otherwise.
    */
   public static putByte(buffer: Uint8Array, offset: number, value: number): boolean {
      if (buffer != null && offset >= 0 && offset < buffer.length) {
         buffer[offset] = value;
         return true;
      } else return false;
   }

   /**
    * Returns a byte array representation of the specified byte value.
    *
    * @param {number} value
    * The value to convert into a byte array.
    * @return {Array} The byte array of the specified value.
    */
   public static convertByte(value: number): number[] {
      return [value];
   }

   /**
    * Reads a short from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset.
    * @return {number} Short value from buffer or 0 on error.
    */
   public static getShort(buffer: Uint8Array, offset: number): number {
      if (buffer != null && offset >= 0 && offset + 1 < buffer.length) {
         let v: number = (<number>(buffer[offset] & 255)) | 0;
         v |= (buffer[offset + 1] & 255) << 8;
         return v;
      } else return 0;
   }

   /**
    * Writes a short into the specified buffer, using little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to write the value to.
    * @param {number} offset
    * Buffer offset.
    * @param {number} value
    * Value to write.
    * @return {boolean} true if successfull, false otherwise.
    */
   public static putShort(buffer: Uint8Array, offset: number, value: number): boolean {
      if (buffer != null && offset >= 0 && offset + 1 < buffer.length) {
         buffer[offset] = (<number>(value & 255)) | 0;
         buffer[offset + 1] = (<number>((value >>> 8) & 255)) | 0;
         return true;
      } else return false;
   }

   /**
    * Returns a byte array representation of the specified short value in little endian byte order.
    *
    * @param {number} value
    * The value to convert into a byte array.
    * @return {Array} The byte array of the specified value.
    */
   public static convertShort(value: number): number[] {
      return [(<number>(value & 255)) | 0, (<number>((value >>> 8) & 255)) | 0];
   }

   /**
    * Reads a 24-bit integer from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset.
    * @return {number} 24-bit integer value from buffer or 0 on error.
    */
   public static getInt24(buffer: Uint8Array, offset: number): number {
      if (buffer != null && offset >= 0 && offset + 2 < buffer.length) {
         let v: number = buffer[offset] & 255;
         v |= (buffer[offset + 1] & 255) << 8;
         v |= (buffer[offset + 2] & 255) << 16;
         if ((v & 8388608) !== 0) v |= -16777216;
         return v;
      } else return 0;
   }

   /**
    * Writes a 24-bit integer into the specified buffer, using little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to write the value to.
    * @param {number} offset
    * Buffer offset.
    * @param {number} value
    * Value to write.
    * @return {boolean} true if successfull, false otherwise.
    */
   public static putInt24(buffer: Uint8Array, offset: number, value: number): boolean {
      if (buffer != null && offset >= 0 && offset + 2 < buffer.length) {
         buffer[offset] = (<number>(value & 255)) | 0;
         buffer[offset + 1] = (<number>((value >>> 8) & 255)) | 0;
         buffer[offset + 2] = (<number>((value >>> 16) & 255)) | 0;
         return true;
      } else return false;
   }

   /**
    * Returns a byte array representation of the specified 24-bit integer value in little endian byte order.
    *
    * @param {number} value
    * The value to convert into a byte array.
    * @return {Array} The byte array of the specified value.
    */
   public static convertInt24(value: number): number[] {
      return [(<number>(value & 255)) | 0, (<number>((value >>> 8) & 255)) | 0, (<number>((value >>> 16) & 255)) | 0];
   }

   /**
    * Reads an integer from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset.
    * @return {number} Integer value from buffer or 0 on error.
    */
   public static getInt(buffer: Uint8Array, offset: number = 0): number {
      // buffer: number[]
      if (buffer != null && offset >= 0 && offset + 3 < buffer.length) {
         let v: number = buffer[offset] & 255;
         v |= (buffer[offset + 1] & 255) << 8;
         v |= (buffer[offset + 2] & 255) << 16;
         v |= (buffer[offset + 3] & 255) << 24;
         return v;
      } else return 0;
   }

   /**
    * Writes an integer into the specified buffer, using little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to write the value to.
    * @param {number} offset
    * Buffer offset.
    * @param {number} value
    * Value to write.
    * @return {boolean} true if successfull, false otherwise.
    */
   public static putInt(buffer: Uint8Array, offset: number, value: number): boolean {
      if (buffer != null && offset >= 0 && offset + 3 < buffer.length) {
         buffer[offset] = (<number>(value & 255)) | 0;
         buffer[offset + 1] = (<number>((value >>> 8) & 255)) | 0;
         buffer[offset + 2] = (<number>((value >>> 16) & 255)) | 0;
         buffer[offset + 3] = (<number>((value >>> 24) & 255)) | 0;
         return true;
      } else return false;
   }

   /**
    * Returns a byte array representation of the specified integer value in little endian byte order.
    *
    * @param {number} value
    * The value to convert into a byte array.
    * @return {Array} The byte array of the specified value.
    */
   public static convertInt(value: number): number[] {
      return [
         (<number>(value & 255)) | 0,
         (<number>((value >>> 8) & 255)) | 0,
         (<number>((value >>> 16) & 255)) | 0,
         (<number>((value >>> 24) & 255)) | 0
      ];
   }

   /**
    * Reads a long from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset.
    * @return {number} Long value from buffer or 0 on error.
    */
   public static getLong(buffer: Uint8Array, offset: number): number {
      if (buffer != null && offset >= 0 && offset + 7 < buffer.length) {
         let v: number = buffer[offset] & 255;
         v |= (buffer[offset + 1] & 255) << 8;
         v |= (buffer[offset + 2] & 255) << 16;
         v |= (buffer[offset + 3] & 255) << 24;
         v |= (buffer[offset + 4] & 255) << 32;
         v |= (buffer[offset + 5] & 255) << 40;
         v |= (buffer[offset + 6] & 255) << 48;
         v |= (buffer[offset + 7] & 255) << 56;
         return v;
      } else return 0;
   }

   /**
    * Writes a long into the specified buffer, using little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to write the value to.
    * @param {number} offset
    * Buffer offset.
    * @param {number} value
    * Value to write.
    * @return {boolean} true if successfull, false otherwise.
    */
   public static putLong(buffer: Uint8Array, offset: number, value: number): boolean {
      if (buffer != null && offset >= 0 && offset + 7 < buffer.length) {
         buffer[offset] = (<number>(value & 255)) | 0;
         buffer[offset + 1] = (<number>((value >>> 8) & 255)) | 0;
         buffer[offset + 2] = (<number>((value >>> 16) & 255)) | 0;
         buffer[offset + 3] = (<number>((value >>> 24) & 255)) | 0;
         buffer[offset + 4] = (<number>((value >>> 32) & 255)) | 0;
         buffer[offset + 5] = (<number>((value >>> 40) & 255)) | 0;
         buffer[offset + 6] = (<number>((value >>> 48) & 255)) | 0;
         buffer[offset + 7] = (<number>((value >>> 56) & 255)) | 0;
         return true;
      } else return false;
   }

   /**
    * Returns a byte array representation of the specified long value in little endian byte order.
    *
    * @param {number} value
    * The value to convert into a byte array.
    * @return {Array} The byte array of the specified value.
    */
   public static convertLong(value: number): number[] {
      return [
         (<number>(value & 255)) | 0,
         (<number>((value >>> 8) & 255)) | 0,
         (<number>((value >>> 16) & 255)) | 0,
         (<number>((value >>> 24) & 255)) | 0,
         (<number>((value >>> 32) & 255)) | 0,
         (<number>((value >>> 40) & 255)) | 0,
         (<number>((value >>> 48) & 255)) | 0,
         (<number>((value >>> 56) & 255)) | 0
      ];
   }

   /**
    *
    * Converts a byte sequence of a buffer into a string.
    *
    * @param {Array} buffer
    * The buffer to read the byte sequence from.
    * @param {number} offset
    * Buffer offset.
    * @param {number} length
    * The number of bytes to convert.
    * @return {string} A string representation of the byte sequence or an empty string on error.
    */
   public static getString(buffer: Uint8Array, offset: number, length: number): string {
      let result = "";
      if (buffer != null && offset >= 0 && offset < buffer.length && length >= 0) {
         result = String(buffer.subarray(offset, offset + length)).replace(/\u0000/g, "");
      }
      return result;
   }

   /**
    * Convenience method to read an unsigned byte value from the specified buffer.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset
    * @return {number} An unsigned byte value.
    */
   public static getUnsignedByte(buffer: Uint8Array, offset: number): number {
      return (<number>(DynamicArray.getByte(buffer, offset) & 255)) | 0;
   }

   /**
    * Convenience method to read an unsigned short value from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset
    * @return {number} An unsigned short value.
    */
   public static getUnsignedShort(buffer: Uint8Array, offset: number): number {
      return ((<number>DynamicArray.getShort(buffer, offset)) | 0) & 65535;
   }

   /**
    * Convenience method to read an unsigned 24-bit integer value from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset
    * @return {number} An unsigned 24-bit integer value.
    */
   public static getUnsignedInt24(buffer: Uint8Array, offset: number): number {
      return DynamicArray.getInt24(buffer, offset) & 16777215;
   }

   /**
    * Convenience method to read an unsigned integer value from the specified buffer in little endian byte order.
    *
    * @param {Array} buffer
    * The buffer to read the value from.
    * @param {number} offset
    * Buffer offset
    * @return {number} An unsigned integer value.
    */
   public static getUnsignedInt(buffer: Uint8Array, offset: number): number {
      return (n => (n < 0 ? Math.ceil(n) : Math.floor(n)))(<number>DynamicArray.getInt(buffer, offset)) & 4294967295;
   }
}
