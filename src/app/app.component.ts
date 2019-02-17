import { Component, Inject } from '@angular/core';
import Creature from './data-access/entities/creature.entity';
import { CreatureService } from './creatures/creature.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    creatures: Creature[] = [];
    displayedColumns: string[] = ['Cre', 'Name'];

    firstName: string = '';
    lastName: string = '';
    age: string = '';

    constructor(private creatureService: CreatureService) {
        this.getCreatures();
    }

    getCreatures(){
        //this.creatureService.findAll().then(creatures => this.creatures = creatures);
        this.creatureService.importFromGame().then(creatures => this.creatures = creatures);
    }

    addCreature(){
        // const user = new User();

        // user.FirstName = this.firstName;
        // user.LastName = this.lastName;
        // user.Age = +this.age;

        // this.databaseService
        //     .connection
        //     .then(() => user.save())
        //     .then(() => {
        //         this.getCreatures();
        //     })
        //     .then(() => {
        //         this.firstName = '';
        //         this.lastName = '';
        //         this.age = '';
        //     })
    }

}
