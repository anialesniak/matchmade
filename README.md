# Matchmade [![CircleCI](https://circleci.com/gh/annterina/matchmade.svg?style=shield&circle-token=826cee8772a4876fe492847fb24c4d8db36a72fc)](https://circleci.com/gh/annterina/matchmade)
Matchmade is a server application that lets you setup your own matchmaking system. With an easy configuration you can empower your server to accept and process client requests and based on them match clients into groups of desired size.

## How it works in general
Matchmade runs as a typical server, however it only accepts one kind of request. Accepted requests are those containing valid client "enrollment" - you can find how to create one [here](#client-requests-format), but it is not essential for understanding how Matchmade works. What's important is that enrollments contain data about the client itself - the features describing client, let's call them **client self data/parameters** and the data the client expects from matched clients - from now let it be called **client searching data/parameters**. Enrolled clients' data is internally kept and processed by the server matchmaking module and used in finding matches between registered clients.

## How matchmaking works in detail

### Matchmaking workflow
Matchmaker module's job is to group enrolled clients into so called _matches_ which are basically groups of clients of size specified in Matchmade configuration file. The match can be created from a given set of clients if all clients are _mutually compatible_. Mutual compatibility means that the self data of the second client matches the searching data of the first client and vice versa. 

> #### Example
> We are using Matchmade to match chess players in pairs so that the players are on a comparable level. Each client is described by one feature - the ranking score representing competence in the game (no matter how it is measured). Therefore we have a matchmaking task for **one parameter client** and **match size of two** Each client also desires to play with another client whose ranking is of comparable value. Therefore in the searching data each client will contain a narrow range of values with the range mean value equal to own ranking. In other words, that means each client is looking for client with ranking slightly lower, slightly higher or equal to own ranking. Hence the 2 clients will make a match if the first clients' ranking lies within the range of rankings searched by the second client and vice versa.

But what if for particular clients there are no matching counterparts in the pool of registered clients? We could leave them for now and wait till some new clients enter the pool, but we could as well wait forever to never have all of those lone clients matched. To prevent it from happening and ensuring each client gets a relatively good match in a reasonable time Matchmade modifies client searching data slightlty after each iteration of finding matches in the existing client pool. This in general results in broadening the search range for each client. To learn how the range is actually expanded and how you can configure it check out [Configuring matchmade](#configuring-matchmade) section.

### Scaling matchmaking problem
The example presented above is the simplest possible - we want to match clients in pairs (smallest possible group) and matching is performed based only on one parameter. Matchmade will handle such task obviously, but it has been created with the greater cause in mind - performing matchmaking for multiple client features concerned when creating matches and for matches consisting of larger groups of clients. This requires a ranged search in a multidimensional space. Matchmade uses k-dimensional tree to perform such searches. Obviously increasing number of features describing a client or matched clients group size will increase the time in which the same number of matches can be found.

## Configuring Matchmade
Configuring Matchmade is the essential part of creating and running your own matchmaking system. When Matchmade server is started it reads the configuration from the `configuration.yaml` file which has to be located in the main directory to which the server has direct access. The settings specified in that file tell Matchmade what kind of matchmaking it needs to perform - how many client parameters will be considered in matching, what are the names of those parameters, their priorities (we will explain their importance soon) and finally what is the size of the group of matched clients.

To show how simple and short is the configuration let's look at the following exemplary `configuration.yaml` contents:

```yaml
parameterCount: 3
teamSize: 3
parameterBaseSteps:
    age: 1
    height: 2
    weight: 2
```

Now let's explain what each line of it means to Matchmade.

### Number of parameters
```yaml
parameterCount: 3
```
Here we set the _**number of parameters describing client**_. The _parameters_, previously also called _features_, are some numerical values describing clients. The number of these parameters and their values is ment to represent some characteristics of the clients relevant to the matchmaking problem. For instance, when using Matchmade for matchmaking online game players into teams we may user two parameters such as `ranking` and `numberOfPlayedGames`. But we could also include some other parameters containing statistics that might represent given player skill. On the other hand if we use Matchmade to create dating app we might what to specify parameters such as `age`, `height` and many others that would represent e.g. given person interests. The number and interpretation of the parameters depends on the domain in which matchmaking will be used and features based on which we want to match clients. It is important to note that **all parameters need to be of numerical value**. So if we want to represent some characteristics which are not numerical, like hair color or favourite tennis player, we will need to discretize them. For example, if we want to specify hair color we may assign 1 to black, 2 to brown, 3 to blond etc.

### Size of matched groups
```yaml
teamSize: 3
``` 
This configuration parameter sets the _**number of clients that will be matched in one group**_. All groups will consist of the same number of clients specified here.

### Parameter names and range search expanding behaviour
```yaml
parameterBaseSteps:
    age: 1
    height: 2
    weight: 2
```
This is not one line of the configuration, but we can treat is as a coherent part. After the `parameterBaseSteps:` line we list all of the parameters by writing their _**name**_ and numerical value called _**base step**_. 

_**Name**_ can be just anything as long as it complies with YAML naming conventions. This name, and I mean exactly this name, will be later expected to be found in JSONs with client requests (read more about it in [Client request format](#client-request-format)).

_**Base step**_ is a value that will be used when Matchmade decides to expand given client searching data (when it is done has been described in [Scaling matchmaking problem](#scaling-matchmaking-problem)). This value is a _minimal_ value of expansion. For instance, if in searching data for a particular parameter we had a range 10-15 and for this parameter base step was set to 2, that range after expansion will be changed to 8-17. You can see that base step should be a positive value - we don't want the ranges to shrink, that won't help Matchmade to find the match, rather the contrary. Not that it is a _minimal_ value of expansion. This value can be magnified by client itself that together with searched parameter values sends their priorities. How they influence expansion rate is described in [Priorities and ranged search expansion](#priorities-and-ranged-search-expansion) section.

## Client request format

### Priorities and ranged search expansion

## Matchmaking results

## License

## Authors