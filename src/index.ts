
import "reflect-metadata";

import { Container } from "typescript-ioc";

import CreatureEditor from "./creature-editor";

const app: CreatureEditor = Container.get(CreatureEditor);
app.start();
