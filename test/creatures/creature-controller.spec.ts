import { expect } from "chai";
import { Context } from "koa";
import "mocha";
import * as sinon from "sinon";
import { anything, capture, instance, mock, verify, when } from "ts-mockito";

import Creature from "../../src/creatures/creature-entity";
import CreatureService from "../../src/creatures/creature-service";
import CreatureController from "../../src/creatures/creature-controller";

import CreatureTestBuilder from "../testutils/creature-test-builder";

describe("CreatureController", () => {
    let controllerUnderTest: CreatureController;
    let creatureService: CreatureService;

    const testId = 80085;
    const creatureWithId: Creature = CreatureTestBuilder.newCreature().withDefaultValues().withId(testId).build();
    const creatureWithoutId: Creature = CreatureTestBuilder.newCreature().withDefaultValues().build();

    beforeEach(() => {
        creatureService = mock(CreatureService);
        controllerUnderTest = new CreatureController(instance(creatureService));
    });

    describe("getAllCreatures", () => {
        it("puts the creatures on the body", async () => {
            const creatures = CreatureTestBuilder.createListOfDefaultCreatures(5);
            when(creatureService.findAll()).thenReturn(Promise.resolve(creatures));
            const ctx: Context = {} as Context;

            await controllerUnderTest.getAllCreatures(ctx);

            expect(ctx.body).to.equal(creatures);
        });
    });

    describe("findCreatureById", () => {

        it("puts the found the found creature on the body", async () => {
            const ctx: Context = {} as Context;
            ctx.params = { id: testId };
            when(creatureService.findById(testId)).thenReturn(Promise.resolve(creatureWithId));

            await controllerUnderTest.findCreatureById(ctx);

            verify(creatureService.findById(testId)).called();
            expect(ctx.body).to.equal(creatureWithId);
        });

        it("return with a 404 if no creature is found", async () => {
            const errorMessage = "No creature found with ID.";
            const ctx: Context = {
                params: { id: testId },
                throw: () => null,
            } as Context;
            when(creatureService.findById(testId)).thenThrow(new Error(errorMessage));
            const ctxMock = sinon.mock(ctx);
            ctxMock.expects("throw").calledWithExactly(404, errorMessage);

            await controllerUnderTest.findCreatureById(ctx);

            ctxMock.verify();
        });
    });

    describe("saveCreature", () => {

        it("delegates to creatureService and responds with 200", async () => {
            const ctx: Context = { request: {} } as Context;
            const requestBody = {
                title: creatureWithoutId.$title,
                releaseYear: creatureWithoutId.$releaseYear,
                duration: creatureWithoutId.$duration,
                rating: creatureWithoutId.$rating,
                seen: creatureWithoutId.$seen,
                director: {
                    id: creatureWithoutId.$director.$id,
                    firstName: creatureWithoutId.$director.$firstName,
                    lastName: creatureWithoutId.$director.$lastName,
                    birthYear: creatureWithoutId.$director.$birthYear,
                },
            };
            ctx.request.body = requestBody;

            when(creatureService.save(anything()))
                .thenReturn(Promise.resolve(creatureWithId));

            await controllerUnderTest.saveCreature(ctx);

            const [firstArg] = capture(creatureService.save).last();
            console.log(JSON.stringify(firstArg));
            expect(firstArg.$id).equals(undefined);
            expect(firstArg.$title).equals(requestBody.title);
            expect(firstArg.$releaseYear).equals(requestBody.releaseYear);
            expect(firstArg.$duration).equals(requestBody.duration);
            expect(firstArg.$rating).equals(requestBody.rating);
            expect(firstArg.$seen).equals(requestBody.seen);
            expect(firstArg.$director.$id).equals(requestBody.director.id);
            expect(firstArg.$director.$firstName).equals(requestBody.director.firstName);
            expect(firstArg.$director.$lastName).equals(requestBody.director.lastName);
            expect(firstArg.$director.$birthYear).equals(requestBody.director.birthYear);

            expect(ctx.body).to.equal(creatureWithId);
        });

    });

});
