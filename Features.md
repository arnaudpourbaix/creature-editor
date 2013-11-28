#Creature Editor for Baldur's Gate

This editor will not edit game's files. Its main purpose is to edit creatures on a rich web user-interface. 
These creatures can be exported in a set of files, containing all changes. Finally, a weidu-script will patch these creatures with exported files.

## Introduction
I know there are several existing tools to edit and/or create creatures but all of them have one of these issues: average GUI, missing features, WEIDU-unfriendly. Let me quickly detail each issue:
- Graphical User Interface. GUI is as important as features. I am not talking about a beautiful GUI, which is always nice to have, but a pratical one. I have chosen a web interface because it's highly configurable. I want to be able to customize my interface with themes and widgets positionning. It should adapt to any screen resolution and navigation should be easy and natural. Drag and drop is also a must and should be very present.
- WEIDU-unfriendly. All editors I know just save changes directly on a CRE file. But what you want to edit an existing file ? Changes must be patched using WEIDU. This editor will not change game files, it will generate a set of files containing all changes. It will include a WEIDU-macro to patch all files.
- Features. When you are working on an ambitious mod, you never seem to find a perfect editor. I have used Near Infinity intensively and while it's a good editor, I was frustated by the lack of features, like beeing able to copy spellbooks and effects between creatures.

## Features

### Organization
Creatures are stored in a database, so it is possible to work with a lot of them but it requires some organization. So, they can be bound to categories.
Categories are representated by a tree and because a creature might have several categories, it's possible to do anything !
For example, I would this:
|- monsters
  |- bear
  |  |- brown bear
  |  |- black bear
|- groups
  |- sarevok
  |- amazons
|- location
  |- naskhel
  |- beregost
  |- temple of bhaal
Now, I would bind Semaj to : sarevok, temple of bhaal. It would allow me to quickly find which creatures compose a group and in which area they could appear.
In any search form, it will be possible to search by one or more categories, including or not children.

Additionnally, you might want to mod on several games so each creature is bound to a game, plain and simple.

Another common issue is mods: you will want different things depending on installed mods. For example, I want to make a spellbook for Davaeorn, I will start with a classic spellbook bound to "vanilla" mod, and then a second spellbook bound to "spell revisions" mod. Both will be stored and WEIDU macro will detect mods and install the right one.
You will also be able to search using mod criteria so you can see, for example, all spellbooks designed for Spell Revisions or missing ones. If you are upgrading to a higher version of a mod, you can use this to locate all entities to update.

Finally, you might want to use different game editions for some attributes. For example, I am editing a bear to make it closer to PnP. Because 2nd edition doesn't give details about strength, constitution, or dexterity, I will use 3rd edition. I could convert on the fly and store 2nd edition only, but I can also store both and conversion will be done by editor.

### States
Attribute values come first from an initial import. I need to know which state it is: original, modified, or final. These states can be applied on creature and other entities too.
When you edit an original value, you automatically create a new modified value. Final state is meant for spellbooks, creatures, scripts... it allows you to filter modified entities that need to be finalized.

### Attributes
Attributes are configurable within a set of tables. Initial configuration is dedicated to Baldur's Gate but it could be adapted for other games. 
Attributes values are well-detailed to prevent any user-input mistakes and all of them are validated before saving.

### Search
It is possible to use every possible criterias and even multiple criteria of the same attribute. In any search, it is possible to specify a context if applicable (organization, game, mod, edition, state).

### Parallel (cool but time-consuming)
It is possible to edit 2 creatures at the same time, useful to compare or copy values between them.