# Matchmade [![CircleCI](https://circleci.com/gh/annterina/matchmade.svg?style=shield&circle-token=826cee8772a4876fe492847fb24c4d8db36a72fc)](https://circleci.com/gh/annterina/matchmade)
Matchmade is a server application that lets you setup your own matchmaking system. With an easy configuration you can empower your server to accept and process client requests and based on them match clients into groups of desired size.

## How it works in general
Matchmade runs as a typical server, however it only accepts one kind of request. Accepted requests are those containing valid client "enrollment" - you can find how to create one [here](#client-requests-format), but it is not essential for understanding how Matchmade works. What's important is that enrollments contain data about the client itself - the features describing client, let's call them **client self data** and the data the client expects from matched clients - from now let it be called **client searching data**. Enrolled clients' data is internally kept and processed by the server matchmaking module and used in finding matches between registered clients.

## How matchmaking works in detail
Matchmaker module's job is to group enrolled clients into groups of size specified in Matchmade configuration file. The match can be created from a given set of clients if all clients are _mutually compatible_. Mutual compatibility means that the self data of the second client _matches_ the searching data of the first client and vice versa. To demonstrate it let's consider very simple example: we are using Matchmade to match chess players in pairs so that the players are on a comparable level. Each client is described by one feature - the ranking score representing competence in the game (no matter how it is measured). Each client also desires to play with another client whose ranking is of comparable value. Therefore in the searching data each client will contain a narrow range of values with the range mean value equal to own ranking. In other words, that means each client is looking for client with ranking slightly lower, slightly higher or equal to own ranking. Hence the 2 clients will make a match if the first clients' ranking lies within the range of rankings searched by the second client and vice versa.

But what if for particular clients there are no matching counterparts in the pool of registered clients? We could leave them for now and wait till some new clients enter the pool, but we could as well wait forever to never have all of those lone clients matched. To prevent it from happening and ensuring each client gets a relatively good match in a reasonable time Matchmade modifies client searching data slightlty after each iteration of finding matches in the existing client pool. This in general results in broadening the search range for each client. To learn how the range is actually expanded and how you can configure it check out [Configuring matchmade](#configuring-matchmade) section.

The presented example is the simplest possible - we want to match clients in pairs (smallest possible group) and matching is performed based only on one parameter. Matchmade will handle such task obviously, but it has been created with the greater cause in mind - performing matchmaking for multiple client features concerned when creating matches and for matches consisting of larger groups of clients. This requires a ranged search in a multidimensional space. Matchmade uses k-dimensional tree to perform such searches. Obviously increasing number of features describing a client or matched clients group size will increase the time in which the same number of matches can be found.

## Configuring Matchmade

## Client requests format

## Matchmaking results

## License

## Authors