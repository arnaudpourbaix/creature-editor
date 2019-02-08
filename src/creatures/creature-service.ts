import { Inject, Singleton } from "typescript-ioc";
import Creature from "./creature-entity";
import CreatureRepository from "./creature-repository";
import GameService from "../infinity-reader/game-service";

@Singleton
export default class CreatureService {

    constructor( @Inject private creatureRepository: CreatureRepository, @Inject private gameService: GameService) { }

    public async findById(id: string): Promise<Creature> {
        return this.creatureRepository.findCreatureById(id);
    }

    public async findAll(): Promise<Creature[]> {
        return this.creatureRepository.getAllCreatures();
    }

    public async importFromGame(): Promise<Creature[]> {
        await this.gameService.openGame();
        return [];
    }

}
