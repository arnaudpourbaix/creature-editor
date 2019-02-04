import { expect } from "chai";
import { Server } from "http";
import "mocha";
import { agent } from "supertest";
import { Container } from "typescript-ioc";
import Creature from "../src/creatures/creature-entity";
import CreatureTestBuilder from "../test/testutils/creature-test-builder";
import CreatureEditor from "../src/creature-editor";

describe("E2E: Creature Actions", () => {

    let app: Server;

    const title: string = "Spongebob Squarepants Creature";
    const rating = 7;
    const duration = 84;
    const releaseYear = 2008;
    const seen = true;
    const directorId = 3;
    const directorFirstName = "John";
    const directorLastName = "Lasseter";
    const directorBirthYear = 1957;

    before(async () => {
        const creatureEditor: CreatureEditor = Container.get(CreatureEditor);
        app = await creatureEditor.start();
    });

    describe("GET /creatures", () => {
        it("returns a list of all creatures", async () => {
            const response = await agent(app)
                .get("/creatures")
                .accept("json")
                .expect(200);

            const result: Creature[] = response.body;
            expect(result).to.have.length(4);
        });
    });

    describe("GET /creatures/:id", () => {

        it("should return the specific creature", async () => {
            const response = await agent(app)
                .get("/creatures/1")
                .accept("json")
                .expect(200);

            const result = response.body;
            expect(result.id).to.equal(1);
            expect(result.title).to.equal("Finding Nemo");
            expect(result.rating).to.equal(7);
            expect(result.releaseYear).to.equal(2008);
            expect(result.duration).to.equal(100);
            expect(result.director.id).to.equal(3);
            expect(result.director.firstName).to.equal("John");
            expect(result.director.lastName).to.equal("Lasseter");
            expect(result.director.birthYear).to.equal(1957);
        });

        it("should return a 404 when the creature with id doesn't exist", async () => {
            const response = await agent(app)
                .get("/creatures/999")
                .accept("json")
                .expect(404);
        });

    });

    describe("POST /creatures", () => {
        it("should add the creature", async () => {
            const response = await agent(app)
                .post("/creatures")
                .accept("json")
                .send(CreatureTestBuilder
                    .newCreature()
                    .withTitle(title)
                    .withRating(rating)
                    .withDuration(duration)
                    .withReleaseYear(releaseYear)
                    .withSeen(seen)
                    .build(),
            ).expect(200);

            const result = response.body;
            expect(result.id).is.greaterThan(0);
        });

        it("should return 400 if the director does not exist yet", async () => {
            await agent(app)
                .post("/creatures")
                .accept("json")
                .send(CreatureTestBuilder
                    .newCreature()
                    .withTitle(title)
                    .withRating(rating)
                    .withDuration(duration)
                    .withReleaseYear(releaseYear)
                    .withSeen(seen)
                    .build(),
            ).expect(400);
        });
    });

    describe("PUT /creatures/:id", () => {

        it("should update the creature with given ID", async () => {
            const creatureResponse = await agent(app)
                .get("/creatures")
                .accept("json")
                .expect(200);
            const allCreatures = creatureResponse.body;
            const creatureId = allCreatures[0].id;

            const response = await agent(app)
                .put(`/creatures/${creatureId}`)
                .accept("json")
                .send(CreatureTestBuilder
                    .newCreature()
                    .withId(creatureId)
                    .withTitle(title)
                    .withRating(rating)
                    .withDuration(duration)
                    .withReleaseYear(releaseYear)
                    .withSeen(seen)
                    .build(),
            ).expect(200);

            const result = response.body;
            expect(result.id).to.equal(1);
            expect(result.title).to.equal(title);
            expect(result.rating).to.equal(rating);
            expect(result.duration).to.equal(duration);
            expect(result.releaseYear).to.equal(releaseYear);
            expect(result.seen).to.equal(seen);
            expect(result.director.id).to.equal(directorId);
            expect(result.director.firstName).to.equal(directorFirstName);
            expect(result.director.lastName).to.equal(directorLastName);
            expect(result.director.birthYear).to.equal(directorBirthYear);
        });

        it("should return 400 if the creature does not exist yet", async () => {
            await agent(app)
                .put("/creatures/99")
                .accept("json")
                .send(CreatureTestBuilder
                    .newCreature()
                    .withId(99)
                    .withTitle(title)
                    .withRating(rating)
                    .withDuration(duration)
                    .withReleaseYear(releaseYear)
                    .withSeen(seen)
                    .build(),
            ).expect(400);
        });

        it("should return 400 if the id of the request object does not match the url id", async () => {
            await agent(app)
                .put("/creatures/10")
                .accept("json")
                .send(CreatureTestBuilder
                    .newCreature()
                    .withId(5)
                    .withTitle(title)
                    .withRating(rating)
                    .withDuration(duration)
                    .withReleaseYear(releaseYear)
                    .withSeen(seen)
                    .build(),
            ).expect(400);
        });
    });

    describe("DELETE /creatures/:id", () => {

        it("should delete the creature with the given type", async () => {
            const creatureResponse = await agent(app)
                .get("/creatures")
                .accept("json")
                .expect(200);
            const currentCreatures = creatureResponse.body;
            const creatureId = currentCreatures[0].id;

            await agent(app)
                .delete(`/creatures/${creatureId}`)
                .accept("json")
                .expect(200);

            const response = await agent(app)
                .get("/creatures")
                .accept("json")
                .expect(200);

            const allCreatures: Creature[] = response.body;

            allCreatures.forEach((element) => {
                expect(element.$id).not.to.equal(creatureId);
            });
        });

        it("should return a 404 if the creature does not exist (anymore)", async () => {
            await agent(app)
                .delete("/creatures/999")
                .accept("json")
                .expect(404);
        });

    });

});
