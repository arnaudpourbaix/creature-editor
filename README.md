# [Creature Editor for Baldur's Gate](https://github.com/arnaudpourbaix/creature-editor/blob/master/Features.md)

## Architecture
It is split in 4 maven submodules that are managed by Spring Boot parent module.

### Core
- contains business entities, repositories, database scripts, services, game files reader.
- uses Hibernate 4 and Liquibase with a HSQLDB database.
- repositories are based on Spring-Data-Jpa

### Front
- is based on [spring-mvc-archetype](https://github.com/kolorobot/spring-mvc-quickstart-archetype) and deployed on an embedded Tomcat server (provided by Spring Boot).
- uses a REST architecture and thus, all client-server messages are in json format.
- contains web project configuration and REST controllers.

### Web
- contains hmtl, css, javascript, and images.
- is based on [ngBoilerPlate](https://github.com/joshdmiller/ng-boilerplate) and [yeoman](https://github.com/yeoman/generator-angular). I prefer ngBoilerPlate but since updates are really slow, I'm using some grunt tasks found in yeoman.
- uses [NodeJS](http://nodejs.org/) and [Grunt](http://gruntjs.com/) to automate everything. 
- is entirely piloted by [Angular](http://angularjs.org/) and uses [jQWidgets](http://www.jqwidgets.com/) for UI components.

### Script Batch
- batch program to generate creature's scripts.

## Prerequisites
* Must have [Java JDK 1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).
* Must have [Maven 3.x](http://maven.apache.org/).
* Must have [node.js (at least v0.10.x)](http://nodejs.org/).
* Must have [Grunt](https://github.com/gruntjs/grunt) node package installed globally.  `npm install -g grunt-cli`
* Must have [Bower](https://github.com/twitter/bower) node package installed globally.  `npm install -g bower`

On Windows OS, Maven can't execute cmd files (npm and grunt). So they need to be duplicated and renamed with bat extension.

## Install

### IDE
1. [Eclipse IDE for Java EE Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplerr).
2. [Plugin EasyShell](http://pluginbox.sourceforge.net). Handfull for calling grunt tasks and running hsqldb server.
3. Add Java 1.7 SDK (Preferences / Installed JREs)

### Parent Project
1. Open Git Repository Exploring
2. Clone a Git Repository and add the clone to this view. 
3. Clone https://github.com/arnaudpourbaix/creature-editor.git and check "import project"
4. Right click on project and select Maven > Update Project..., check all sub-modules

### Core project
1. Start database, can be done using `start.cmd` in launcher folder (in parent project, right click on `start.cmd` and select Easy Shell > Run...)
2. Create and run a maven configuration to create/update database: `liquibase:update` (Base directory: `${workspace_loc:/creature-editor-core}`). This will create database and update it on further executions.
3. Create a java configuration to start web application (Project: `creature-editor-front`, Main class: `com.pourbaix.creature.editor.web.spring.WebAppInitializer`).

### Web project
1. Project Properties, JavaScript, Include Path, Source, included, Edit, Add `src/app`
2. Open a shell command in project root directory   
3. `npm install`
4. `bower install`

### Run it
1. Start Web application
2. Open a shell command in web project root directory 
3. `grunt serve`
4. Open url `http://localhost:9000` in a browser
