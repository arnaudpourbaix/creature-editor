import { Inject, Singleton } from "typescript-ioc";
import GameService from "../infinity-reader/game-service";
import KeyfileService from "../infinity-reader/keyfile-service";
import Creature from "./creature-entity";
import CreatureRepository from "./creature-repository";
import { ExtensionEnum } from "../infinity-reader/constants";

@Singleton
export default class CreatureService {
    constructor(@Inject private creatureRepository: CreatureRepository, @Inject private gameService: GameService, @Inject private keyfileService: KeyfileService) {}

    public async findById(id: string): Promise<Creature> {
        return this.creatureRepository.findCreatureById(id);
    }

    public async findAll(): Promise<Creature[]> {
        return this.creatureRepository.getAllCreatures();
    }

    public async importFromGame(): Promise<Creature[]> {
        await this.gameService.openGame();
        let cres = this.keyfileService.getResourceEntriesByExtension(ExtensionEnum.Creature);
        return cres.map(c => {
            let creature = new Creature();
            creature.cre = c.resourceName;
            creature.name = c.resourceName;
            return creature;
        });
    }
}
