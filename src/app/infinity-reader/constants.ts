export enum ExtensionEnum {
    Item = "ITM",
    Spell = "SPL",
    Script = "BCS",
    Identifiers = "IDS",
    Creature = "CRE",
    Dialog = "DLG",
    Data = "2DA",
    Store = "STO",
    Effect = "EFF",
    Projectile = "PRO",
}

export const EXTENSIONS = [
   { key: 0x3ed, value: ExtensionEnum.Item },
   { key: 0x3ee, value: ExtensionEnum.Spell },
   { key: 0x3ef, value: ExtensionEnum.Script },
   { key: 0x3f0, value: ExtensionEnum.Identifiers },
   { key: 0x3f1, value: ExtensionEnum.Creature },
   { key: 0x3f3, value: ExtensionEnum.Dialog },
   { key: 0x3f4, value: ExtensionEnum.Data },
   { key: 0x3f6, value: ExtensionEnum.Store },
   { key: 0x3f8, value: ExtensionEnum.Effect },
   { key: 0x3fd, value: ExtensionEnum.Projectile }
];

