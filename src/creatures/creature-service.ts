import { Inject, Singleton } from "typescript-ioc";
import Creature from "./creature-entity";
import CreatureRepository from "./creature-repository";

@Singleton
export default class MovieService {

    constructor( @Inject private creatureRepository: CreatureRepository) { }

    public async findById(id: string): Promise<Creature> {
        return this.creatureRepository.findCreatureById(id);
    }

    public async findAll(): Promise<Creature[]> {
        return this.creatureRepository.getAllCreatures();
    }

}
