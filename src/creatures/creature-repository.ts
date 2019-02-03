import { Singleton } from "typescript-ioc";
import EntityNotFoundError from "../exceptions/EntityNotFoundError";
import Creature from "./creature-entity";
import IRepository from "../models/IRepository";

@Singleton
export default class CreatureRepository extends IRepository {

    public async getAllCreatures(): Promise<Creature[]> {
        return this.getCreatureRepository()
            .find();
    }

    public async findCreatureById(id: string): Promise<Creature> {
        const result = await this.getCreatureRepository()
            .findOne(id);
        if (!result) {
            throw new EntityNotFoundError();
        }
        return result;
    }

}
