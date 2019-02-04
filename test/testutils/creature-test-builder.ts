import Creature from "../../src/creatures/creature-entity";

export default class CreatureTestBuilder {

    private creature: Creature = new Creature();

    public static newCreature() {
        return new CreatureTestBuilder();
    }

    public withId(id: number): CreatureTestBuilder {
        this.creature.$id = id;
        return this;
    }
    public withTitle(title: string): CreatureTestBuilder {
        this.creature.$title = title;
        return this;
    }
    public withReleaseYear(releaseYear: number): CreatureTestBuilder {
        this.creature.$releaseYear = releaseYear;
        return this;
    }
    public withDuration(duration: number): CreatureTestBuilder {
        this.creature.$duration = duration;
        return this;
    }
    public withRating(rating: number): CreatureTestBuilder {
        this.creature.$rating = rating;
        return this;
    }
    public withSeen(seen: boolean): CreatureTestBuilder {
        this.creature.$seen = seen;
        return this;
    }

    public withDefaultValues(): CreatureTestBuilder {
        return this
            .withTitle("title")
            .withDuration(100)
            .withRating(5)
            .withReleaseYear(2016)
            .withSeen(true);
    }

    public build(): Creature {
        return this.creature;
    }

    public static createListOfDefaultCreatures(size: number) {
        const result = [];
        for (let i = 0; i < size; i++) {
            result.push(CreatureTestBuilder.newCreature().withDefaultValues().withId(Math.random() * 10).build());
        }
        return result;
    }
}
