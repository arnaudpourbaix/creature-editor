import * as Koa from "koa";
import * as bodyParser from "koa-bodyparser";
import * as logger from "koa-logger";
import * as Router from "koa-router";

import { createConnection } from "typeorm";
import { Inject } from "typescript-ioc";

import CreatureRoutes from "./creatures/creature-routes";

export default class CreatureEditor {

    constructor(@Inject private creatureRoutes: CreatureRoutes) { }

    private async createApp() {
        await createConnection();

        const app: Koa = new Koa();
        const router: Router = new Router();

        this.creatureRoutes.register(router);

        app.use(logger());
        app.use(bodyParser());
        app.use(router.routes());
        app.use(router.allowedMethods());

        return Promise.resolve(app);
    }

    public async start() {
        const app = await this.createApp();
        console.log("Started listening on port 3000...");
        const server = app.listen(3000);
        return Promise.resolve(server);
    }

}
