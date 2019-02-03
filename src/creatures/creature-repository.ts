import { Singleton } from "typescript-ioc";
import EntityNotFoundError from "../exceptions/EntityNotFoundError";
import Creature from "./creature-entity";
import { getManager } from "typeorm";

@Singleton
export default class CreatureRepository {

    public async getAllCreatures(): Promise<Creature[]> {
        return getManager().getRepository(Creature).find();
    }

    public async findCreatureById(id: string): Promise<Creature> {
        const result = await getManager().getRepository(Creature).findOne(id);
        if (!result) {
            throw new EntityNotFoundError();
        }
        return result;
    }

}
