import { Entity, PrimaryColumn, Column, BaseEntity } from "typeorm";

@Entity()
export default class Creature extends BaseEntity {

    @PrimaryColumn()
    cre: string;

    @Column()
    name: string;

}
