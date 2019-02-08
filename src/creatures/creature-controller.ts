import { IRouterContext } from "koa-router";
import { Inject, Singleton } from "typescript-ioc";
import CreatureService from "./creature-service";

@Singleton
export default class CreatureController {

    constructor( @Inject private creatureService: CreatureService) { }

    public async getAllCreatures(ctx: IRouterContext) {
        ctx.body = await this.creatureService.findAll();
    }

    public async findCreatureById(ctx: IRouterContext) {
        try {
            ctx.body = await this.creatureService.findById(ctx.params.id);
        } catch (e) {
            ctx.throw(404);
        }
    }

    public async importFromGame(ctx: IRouterContext) {
        try {
            ctx.body = await this.creatureService.importFromGame();
        } catch (e) {
            ctx.throw(400, e);
        }
    }

}
