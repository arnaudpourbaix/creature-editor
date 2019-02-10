export class ResourceEntry {
    resourceName: string;
	extension: string;
	overrideFile: string;
    resourceData: string;
	resourceTextData: string;
    searchString: string;

    constructor(filename: string) {
        filename = filename.toUpperCase();
        this.resourceName = filename;
        this.extension = filename.slice(-3);
    }
}

export class BiffResourceEntry {
    overrideFile: string;

    constructor(filename: string) {
    }
}
