# Matchmade [![CircleCI](https://circleci.com/gh/annterina/matchmade.svg?style=shield&circle-token=826cee8772a4876fe492847fb24c4d8db36a72fc)](https://circleci.com/gh/annterina/matchmade)
Matchmade is a server application that lets you setup your own matchmaking system. With an easy configuration you can empower your server to accept and process client requests and based on them match clients into groups of desired size.

## Table of contents
1. [How it works in general](#how-it-works-in-general)
2. [How matchmaking works in detail](#how-matchmaking-works-in-detail)
3. [Configuring Matchmade](#configuring-matchmade)
4. [Client request](#client-request)
5. [Matchmaking results](#matchmaking-results)
6. [License](#license)
7. [Authors](#authors)

## How it works in general
Matchmade runs as a typical server, however it only accepts one kind of request. Accepted requests are those containing valid client "enrollment" - you can find how to create one in [Client request](#client-request) section, but it is not essential for understanding how Matchmade works. What's important is that enrollments contain data about the client itself - the features describing client, let's call them **client self data/parameters** and the data the client expects from matched clients - from now let it be called **client searching data/parameters**. Enrolled clients' data is internally kept and processed by the server matchmaking module and used in finding matches between registered clients.

## How matchmaking works in detail

### Matchmaking workflow
Matchmaker module's job is to group enrolled clients into so called _matches_ which are basically groups of clients of size specified in Matchmade configuration file. The match can be created from a given set of clients if all clients are _mutually compatible_. Mutual compatibility means that the self data of the second client matches the searching data of the first client and vice versa. 

> #### Example
> We are using Matchmade to match chess players in pairs so that the players are on a comparable level. Each client is described by one feature - the ranking score representing competence in the game (no matter how it is measured). Therefore we have a matchmaking task for **one parameter client** and **matched clients group size of two**. Each client also desires to play with another client whose ranking is of comparable value. Therefore in the searching data each client will contain a narrow range of values with the range mean value equal to own ranking. In other words, that means each client is looking for client with ranking slightly lower, slightly higher or equal to own ranking. Hence the 2 clients will make a match if the first clients' ranking lies within the range of rankings searched for by the second client and vice versa.

But what if for particular clients there are no matching counterparts in the pool of registered clients? We could leave them for now and wait till some new clients enter the pool, but we could as well wait forever to never have all of those lone clients matched. To prevent it from happening and ensuring each client gets a relatively good match in a reasonable time Matchmade modifies client searching data slightly after each iteration of finding matches in the existing client pool. This in general results in broadening the search range for each client. To learn how the range is actually expanded and how you can configure it check out [Configuring matchmade](#configuring-matchmade) section.

### Scaling matchmaking problem
The example presented above is the simplest possible - we want to match clients in pairs (smallest possible groups) and matching is performed based only on one parameter. Matchmade will handle such task obviously, but it has been created with the greater cause in mind - performing matchmaking for multiple client features concerned when creating matches and for matches consisting of larger groups of clients. This requires a ranged search in a multidimensional space. Matchmade uses k-dimensional tree to perform such searches. Obviously increasing number of features describing a client or matched clients group size will increase the time in which the same number of matches can be found.

## Configuring Matchmade
Configuring Matchmade is the essential part of creating and running your own matchmaking system. When Matchmade server is started it reads the configuration from the `configuration.yaml` file which has to be located in the main directory to which the server has direct access. The settings specified in that file tell Matchmade what kind of matchmaking it needs to perform - how many client parameters will be considered in matching, what are the names of those parameters, their priorities (we will explain their importance soon) and finally what is the size of the group of matched clients.

To show how simple and short the configuration is let's take a look at the following exemplary `configuration.yaml` content:

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
Here we set the _**number of parameters describing client**_. The _parameters_, previously also called _features_, are some numerical values describing clients. The number of these parameters and their values are meant to represent some characteristics of the clients relevant to the matchmaking problem. For instance, when using Matchmade for matchmaking online game players into teams we may user two parameters such as `ranking` and `numberOfPlayedGames`. But we could also include some other parameters containing statistics that might represent given player skill. On the other hand if we use Matchmade to create dating app we might want to specify parameters such as `age`, `height` and many others that would represent e.g. given person interests. The number and interpretation of the parameters depends on the domain in which matchmaking will be used and features based on which we want to match clients. It is important to note that **all parameters need to be of numerical value**. So if we want to represent some characteristics which are not numerical, like hair color or favourite tennis player, we will need to discretize them. For example, if we want to specify hair color we may assign 1 to black, 2 to brown, 3 to blond etc.

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

_**Name**_ can be just anything as long as it complies with YAML naming conventions. This name, and I mean exactly this name, will be later expected to be found in JSONs with client requests (read more about it in [Client request](#client-request)).

_**Base step**_ is a value that will be used when Matchmade decides to expand given client searching data (when it is done has been described in [Matchmaking workflow](#matchmaking-workflow)). This value is a _minimal_ value of expansion. For instance, if in searching data for a particular parameter we had a range 10-15 and for this parameter base step was set to 2, that range after expansion will be changed to 8-17. You can see that base step should be a positive number - we don't want the ranges to shrink, that won't help Matchmade to find the match, rather the contrary. Also note that it is a _minimal_ value of expansion. This value can be multiplied by client itself that together with searched parameter values sends their priorities. How they influence expansion rate is described in [Priorities and ranged search expansion](#priorities-and-ranged-search-expansion) section.

## Client request

Matchmade server on its own doesn't do anything special - it needs to have some data about clients to be able to create matches from those clients. The data about clients can be sent directly to running Matchmade server through POST HTTP requests. Each of those requests should contain JSON representing client data - both self parameters and searching parameters. Also the JSON format should comply with the parameter names that were specified in Matchmade configuration file. Sounds complicated? Then let's dive into the example to prove you wrong

### Example description

Let's suppose we are running Matchmade to match tennis players that are on a similar level. We will match them based on 4 parameters:

* **age** - each client will specify how old his opponent should be by giving the range of values
* **win/loose ratio** - each player has a concrete value of his/her W/L ratio and will specify in what range the W/L ratio of his/her opponent should be
* **ranking** - players have their rankings that can be determined by their win/loose ratio
* **self rating of skill** - each player will assess his own skill on a scale from 1 to 5

To run Matchmade we prepared the following configuration (if you don't know yet how Matchmade is configured go to [Configuring Matchmade](#configuring-matchmade)):

```yaml
parameterCount: 4
teamSize: 2
parameterBaseSteps:
    age: 0
    winLooseRatio: 0.01
    ranking: 5
    selfRating: 0
```

The number of parameters is obvious as we already decided that there will be 4 of them. Team size is determined by the nature of our matchmaking problem (matching tennis players) - we need 2 players to get matched so that they can play together.

Base steps require some more explanation. But first we need to introduce possible **client searching parameter types** and there are 4 of them.

### Client searching parameter types

* **non-scalable fixed** - setting searching parameter to this type means that we expect clients matched to this client to have their _self parameter values exactly the same as specified_ here. In our example **self rating** will be of this type, because we want the matched players to rate their skills equally (this may tell something both about their skill and confidence)
* **scalable fixed** - when we set parameter to this type we want to find other clients with _self parameter close to specified value_. In our tennis example **ranking** will be of this type, because we want the matched players to have ranking as close as possible to our ranking (which will be copied from self parameters to search parameters).
* **non-scalable ranged** - if the parameter is set to this type Matchmade will find clients with _self parameter belonging to specified range (inclusive)_, but will not expand this range during execution when matches could not be found. Coming back to the tennis example, **age** will be of this type, because we want players to be sure with how old opponents they can be matched, but we want to let them specify more than one concrete age (which would be quite limiting).
* **scalable ranged** - setting searching parameter to this type tells Matchmade to find clients with _self parameter belonging to specified range (inclusive)_, like with non-scalable ranged type, but it also allows Matchmade to expand the search range if the match could not be found. In our example, **winLooseRatio** will be of that type, as broadening the range of this values slightly will not affect players satisfaction with the match, but will help Matchmade to find the matches faster.

**Fixed** types will contain `value` field simply holding value for given search parameter.

**Ranged** types will contain `lower` and `upper` fields storing the inclusive boundaries of the search parameter range.

Both **scalable** types will contain information field `priority` (explained below).

### Priorities and ranged search expansion

Now, when we know what are the possible types of parameters the base step value can be explained. If a parameter is of scalable type (**scalable fixed** or **scalable ranged**) and match for a given client could not be found in the current client pool, Matchmade will **expand searching parameter _at least_ by the base step value**. Why at least? Because with scalable parameters come parameter `priority` fields specified in sent client request. This are basically the multipliers of the base step. So if the base step is for `ranking` is set to 5, but client sets in searching parameters priority of `ranking` to 3, when the searching parameter is expanded it will be expanded by 3*5=15, instead of just base step value (5).

### JSON format

Before we finally show how client request JSON can look like in our example we need to know one more thing and stress the second. We need to know, that **client self parameters are always of non-scalable fixed type**. That is because, as it was said at the very beginning, clients are described by **concrete numerical values representing their features**. And what we need to stress - **client request consist of self data and searching data**. Without further ado let's take a look at exemplary valid client request:

```json
{
  "clientSelf" : {
    "age" : {
      "type" : "nonScalableFixed",
      "value" : 21
    },
    "winLooseRatio" : {
      "type" : "nonScalableFixed",
      "value" : 1.46
    },
    "ranking" : {
      "type" : "nonScalableFixed",
      "value" : 1465
    },
    "selfRating" : {
      "type" : "nonScalableFixed",
      "value" : 3
    }
  },
  "clientSearching" : {
    "age" : {
      "type" : "nonScalableRanged",
      "lower" : 19,
      "upper" : 22
    },
    "winLooseRatio" : {
      "type" : "scalableRanged",
      "lower" : 1.4,
      "upper" : 1.6,
      "priority" : 5
    },
    "ranking" : {
      "type" : "scalableFixed",
      "value" : 1465,
      "priority" : 3
    },
    "selfRating" : {
      "type" : "nonScalableFixed",
      "value" : 3
    }
  }
}
```

### Important notes

As far as for the `clientSelf` parameters we are restricted to use only non-scalable fixed type, there is no such restriction for `clientSearching` ones, where we can use all 4 types. 

Note that in server configuration **we do not specify what are the expected types** of given parameters. All we is set base steps so that **in case** a parameter is expanded, it will be expanded at least by this value. However clients sending requests to server may assign any type to each searching parameter, also the non-scalable types. Even though a base step might be set to such parameter to some number bigger than 0, non-scalable parameter will never be expanded.

Quite the opposite scenario is also possible - to set base step to 0 in configuration for parameters that client decided to send as ones of scalable type. Then even though it has expandable type we decided to enforce NOT expanding particular parameter by setting base step to 0 in configuration.

This leads to the conclusion that Matchmade **configuration is in some way superior to client requests parameter types**. In configuration we specify if we allow for parameter expansion (base step bigger than 0) or not (base step equal to 0) and if we do, what is the minimal expansion value. Client can assign a given searching parameter any type deciding between fixed and ranged value and scalable and non-scalable behaviour. However the scalable behaviour may be blocked by the server configuration if such parameter has base step set to 0.

Usually all clients will use the same set of searching parameters with same types associated with specific parameters. This will be enforced by the nature of the modeled problem or expecting common behaviour and fairly similar way in which matches are created. However Matchmade allows more flexible approach if you ever need to use it that way.

## Matchmaking results

When Matchmade founds the match between clients in its client pool it logs this success to standard server output. Matchmade server uses internal client identification system - each client is simply assigned a unique number. The logged results will consist of information which clients have been matched identifying clients by those values.

## License

Matchmade is made publicly available with [BSD 2-Clause License](https://github.com/annterina/matchmade/blob/master/LICENSE).

## Authors

Anna Leśniak [annterina](https://github.com/annterina)

Kacper Leśniak [KacperKenjiLesniak](https://github.com/KacperKenjiLesniak)

Jakub Gwizdała [Bzdeco](https://github.com/Bzdeco)

Łukasz Ściga [OatmealLick](https://github.com/OatmealLick)