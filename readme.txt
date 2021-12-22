High Level Arctitecture Changes
1. Transfer from Heroku to Azure hosted
2. Split back end into multiple services. Rough brainstorming below, but much more analysis needed.
    2a. game-updater which updates pga tournament scores and possibly team scores for IN_PROGRESS games
    2b. game-manager which handles tournament/game creation, admin,
    2b. game-retriever which handles requests from front end for login, logout, front end population queries (game history, on going games, drafts?). Needs to be horizontally scaleable
