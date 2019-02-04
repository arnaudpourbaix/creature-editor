import { expect } from "chai";
import "mocha";
import "sinon-chai";
import { instance, mock, verify, when } from "ts-mockito";

import Creature from "../../src/creatures/creature-entity";
import CreatureRepository from "../../src/creatures/creature-repository";
import CreatureService from "../../src/creatures/creature-service";

import CreatureTestBuilder from "../testutils/creature-test-builder";

describe("CreatureService", () => {

    let serviceUnderTest: CreatureService;
    let creatureRepository: CreatureRepository;

    const creatureId = "WOLF";
    const creatureList = CreatureTestBuilder.createListOfDefaultCreatures(5);
    const creatureWithId = CreatureTestBuilder.newCreature().withDefaultValues().withId(creatureId).build();
    const creatureWithoutId = CreatureTestBuilder.newCreature().withDefaultValues().build();

    beforeEach(() => {
        creatureRepository = mock(CreatureRepository);

        serviceUnderTest = new CreatureService(
            instance(creatureRepository),
        );
    });

    describe("findAll", () => {
        it("should return all the creatures", async () => {
            when(creatureRepository.getAllCreatures()).thenReturn(Promise.resolve(creatureList));
            const result = await serviceUnderTest.findAll();
            expect(result).to.equal(creatureList);
        });
    });

    describe("findById", () => {
        it("should return the creature with given ID", async () => {
            when(creatureRepository.findCreatureById(creatureId)).thenReturn(Promise.resolve(creatureWithId));
            const actual = await serviceUnderTest.findById(creatureWithId.$id);
            expect(actual).to.equal(creatureWithId);
        });

    });

    describe("saveCreature", () => {
        it("should save the given creature", async () => {
            when(creatureRepository.saveCreature(creatureWithoutId)).thenReturn(Promise.resolve(creatureWithId));
            const actual = await serviceUnderTest.save(creatureWithoutId);
            expect(actual).to.equal(creatureWithId);
        });
    });

    describe("deleteCreature", () => {
        it("should delete the given creature", async () => {
            when(creatureRepository.deleteCreature(creatureId)).thenReturn(Promise.resolve());
            const actual = await serviceUnderTest.delete(creatureId);
            verify(creatureRepository.deleteCreature(creatureId)).called();
        });
    });

});
