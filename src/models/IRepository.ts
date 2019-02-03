import { getManager } from "typeorm";
import Creature from "../creatures/creature-entity";

export default abstract class IRepository {

    protected getCreatureRepository() {
        return getManager().getRepository(Creature);
    }

}
