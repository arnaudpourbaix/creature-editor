import { DatabaseService } from './../data-access/database.service';
import { Injectable } from "@angular/core";
import { GameService } from "../infinity-reader/game.service";
import { KeyfileService } from "../infinity-reader/keyfile.service";
import Creature from "../data-access/entities/creature.entity";
import { ExtensionEnum } from "../infinity-reader/constants";

@Injectable({
   providedIn: "root"
})
export class CreatureService {
    constructor(private databaseService: DatabaseService, private gameService: GameService, private keyfileService: KeyfileService) {}

    public async findById(id: string): Promise<Creature> {
        return this.databaseService
            .connection
            .then(() => Creature.findOne(id));
    }

    public async findAll(): Promise<Creature[]> {
        return this.databaseService
            .connection
            .then(() => Creature.find());
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
