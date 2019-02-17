import { app, BrowserWindow, screen } from "electron";
import * as path from "path";
import * as url from "url";

let win: BrowserWindow;

// detect serve mode
const args = process.argv.slice(1);
let serve: boolean = args.some(val => val === "--serve");

function createWindow() {
   const size = screen.getPrimaryDisplay().workAreaSize;

   win = new BrowserWindow({ x: 0, y: 0, width: size.width, height: size.height });

   if (serve) {
      // get dynamic version from localhost:4200
      require("electron-reload")(__dirname, {
         electron: require(`${__dirname}/node_modules/electron`)
      });
      win.loadURL("http://localhost:4200");
   } else {
      // load the dist folder from Angular
      win.loadURL(
         url.format({
            pathname: path.join(__dirname, `/dist/index.html`),
            protocol: "file:",
            slashes: true
            //icon: path.join(__dirname, 'assets/icons/favicon.png')
         })
      );
   }

   // The following is optional and will open the DevTools:
   win.webContents.openDevTools();

   win.on("closed", () => {
      win = null;
   });
}

try {
   // This method will be called when Electron has finished
   // initialization and is ready to create browser windows.
   // Some APIs can only be used after this event occurs.
   app.on("ready", createWindow);

   // Quit when all windows are closed.
   app.on("window-all-closed", () => {
      // On OS X it is common for applications and their menu bar
      // to stay active until the user quits explicitly with Cmd + Q
      if (process.platform !== "darwin") {
         app.quit();
      }
   });

   // initialize the app's main window
   app.on("activate", () => {
      if (win === null) {
         createWindow();
      }
   });
} catch (e) {
   // Catch Error
   // throw e;
}
