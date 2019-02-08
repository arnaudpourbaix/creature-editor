import { IRouterContext } from "koa-router";
import { Inject } from "typescript-ioc";
import CreatureController from "./creature-controller";
import Route from "../models/route";
import BaseRoutes from "../models/base-routes";

export default class CreatureRoutes extends BaseRoutes {

    constructor( @Inject private creatureController: CreatureController) {
        super();
    }

    protected getRoutes(): Route[] {
        return [
            Route.newRoute("/creatures", "get", (ctx: IRouterContext) => this.creatureController.getAllCreatures(ctx)),
            Route.newRoute("/creatures/import", "get", (ctx: IRouterContext) => this.creatureController.importFromGame(ctx)),
            Route.newRoute("/creatures/:id", "get", (ctx: IRouterContext) => this.creatureController.findCreatureById(ctx))
        ];
    }
}
