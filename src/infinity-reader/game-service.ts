import fs = require('fs');
import path = require('path');

class GameService {

    private GAME_FOLDER = "D:\\Games\\Beamdog\\00766";
    private DEFAULT_LANGUAGE = "en_US";
    private KEY_FILENAME = "chitin.key";
    private DIALOG_FILENAME = "dialog.tlk";

	public openGame() {
		this.fetchDialogFile();
		// ResourceFactory.getInstance().setGameVersion(globalContext.getGameVersion());
		// ResourceFactory.getInstance().setRootDirs(globalContext.getRootDirectories());
		this.loadResources();
	}

	private fetchDialogFile() {
        let file = path.format({ root: this.GAME_FOLDER, dir: "lang", name: this.DIALOG_FILENAME });
        fs.readFile(file, (buffer) => {

        });
	}

	private loadResources() {
        //StringResource.getInstance().init(globalContext.getDialogFile());
        // Keyfile.getInstance().init(globalContext.getChitinKey());
		// // Add override resources
		// for (final File rootDir : globalContext.getRootDirectories()) {
		// 	File overrideDir = new File(rootDir, Constant.OVERRIDE_DIRECTORY);
		// 	if (!overrideDir.exists() || !overrideDir.isDirectory()) {
		// 		continue;
		// 	}
		// 	for (final File overrideFile : overrideDir.listFiles()) {
		// 		if (overrideFile.isDirectory()) {
		// 			continue;
		// 		}
		// 		String filename = overrideFile.getName().toUpperCase();
		// 		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(filename);
		// 		if (entry == null) {
		// 			ResourceEntry fileEntry = new FileResourceEntry(overrideFile);
		// 			Keyfile.getInstance().addResourceEntry(fileEntry);
		// 		} else if (entry instanceof BiffResourceEntry) {
		// 			((BiffResourceEntry) entry).setOverrideFile(overrideFile);
		// 		}
		// 	}
		// }
	}

}