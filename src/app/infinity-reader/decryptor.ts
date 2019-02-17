export default class Decryptor {
   private Decryptor() {}

   static key: string[] = [
      String.fromCharCode(136),
      String.fromCharCode(168),
      String.fromCharCode(143),
      String.fromCharCode(186),
      String.fromCharCode(138),
      String.fromCharCode(211),
      String.fromCharCode(185),
      String.fromCharCode(245),
      String.fromCharCode(237),
      String.fromCharCode(177),
      String.fromCharCode(207),
      String.fromCharCode(234),
      String.fromCharCode(170),
      String.fromCharCode(228),
      String.fromCharCode(181),
      String.fromCharCode(251),
      String.fromCharCode(235),
      String.fromCharCode(130),
      String.fromCharCode(249),
      String.fromCharCode(144),
      String.fromCharCode(202),
      String.fromCharCode(201),
      String.fromCharCode(181),
      String.fromCharCode(231),
      String.fromCharCode(220),
      String.fromCharCode(142),
      String.fromCharCode(183),
      String.fromCharCode(172),
      String.fromCharCode(238),
      String.fromCharCode(247),
      String.fromCharCode(224),
      String.fromCharCode(202),
      String.fromCharCode(142),
      String.fromCharCode(234),
      String.fromCharCode(202),
      String.fromCharCode(128),
      String.fromCharCode(206),
      String.fromCharCode(197),
      String.fromCharCode(173),
      String.fromCharCode(183),
      String.fromCharCode(196),
      String.fromCharCode(208),
      String.fromCharCode(132),
      String.fromCharCode(147),
      String.fromCharCode(213),
      String.fromCharCode(240),
      String.fromCharCode(235),
      String.fromCharCode(200),
      String.fromCharCode(180),
      String.fromCharCode(157),
      String.fromCharCode(204),
      String.fromCharCode(175),
      String.fromCharCode(165),
      String.fromCharCode(149),
      String.fromCharCode(186),
      String.fromCharCode(153),
      String.fromCharCode(135),
      String.fromCharCode(210),
      String.fromCharCode(157),
      String.fromCharCode(227),
      String.fromCharCode(145),
      String.fromCharCode(186),
      String.fromCharCode(144),
      String.fromCharCode(202)
   ];

   public static decrypt(buffer: number[], offset: number, bytesread: number): string {
      let decoff: number = 0;
      let chars: number[] = (s => {
         let a = [];
         while (s-- > 0) a.push(0);
         return a;
      })(bytesread - offset);
      for (let i: number = offset; i < bytesread; i++) {
         chars[i - offset] =
            (<number>((256 + ((<number>buffer[i]) | 0)) ^ (c => (c.charCodeAt == null ? <any>c : c.charCodeAt(0)))(Decryptor.key[decoff++]))) | 0;
         if (decoff === Decryptor.key.length) {
            decoff = 0;
         }
      }
      return String.fromCharCode.apply(null, chars);
   }
}
