import {Entity, Column, PrimaryColumn} from "typeorm";

@Entity()
export default class Creature {

    @PrimaryColumn()
    cre: string;

    @Column()
    name: string;

}
