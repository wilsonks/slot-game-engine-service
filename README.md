# slot-game-engine-service

A 3x3 classic slot game engine with paytable and bonus games, for the `slot-central` microservices platform.

Pure, stateless spin computation: RNG, virtual reel strips, primary game paytable, and secondary/bonus games (Money Wheel, Hit-or-Miss, One-of-Three, Six-of-Eighteen). No side effects, no database calls, no wallet mutation — this is the certified math core, called by `slot-game-controller-service`.

This repository is part of a re-architecture of the `slot-central-server-express-rmq` Node.js EGM slot-floor backend into Spring Boot microservices.

Scaffolding in progress.
