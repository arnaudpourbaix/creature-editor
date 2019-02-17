import {Injectable} from '@angular/core';
import {Connection, ConnectionOptions, createConnection} from 'typeorm';
import {Settings} from './repositories/settings';
import {User} from './entities/user.entity';
import Creature from './entities/creature.entity';

@Injectable({
    providedIn: 'root'
})
export class DatabaseService {

    public connection: Promise<Connection>;
    private readonly options: ConnectionOptions;

    constructor() {
        Settings.initialize();
        this.options = {
            type: "sqlite",
            database: Settings.dbPath,
            entities: [User, Creature],
            synchronize: true,
            logging: false,
        };
        this.connection = createConnection(this.options);
    }
}
