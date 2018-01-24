import groovy.json.JsonBuilder
import org.testng.reporters.Files

int numberOfGeneratedExtras = 99;
String directory = "../src/integration-test/resources/director"

List<String> clients = new ArrayList<>()
for(int i = 0; i < numberOfGeneratedExtras; i++) {
    clients.add(generateExtra())
}

// Matching extra
def matchingBuilder = new JsonBuilder()
def matchingJson = matchingBuilder {
    clientSelf {
        ranking {
            type "nonScalableFixed"
            value 2002
        }
        score  {
            type "nonScalableFixed"
            value 1002
        }
    }
    clientSearching {
        ranking {
            type  "scalableRanged"
            lower 100
            upper 200
            priority 1
        }
        score {
            type "scalableRanged"
            lower 100
            upper 200
            priority 1
        }
    }
}
clients.add(matchingBuilder.toString())

// Director
def directorBuilder = new JsonBuilder()
def directorJson = directorBuilder {
    clientSelf {
        ranking {
            type "nonScalableFixed"
            value 150
        }
        score  {
            type "nonScalableFixed"
            value 150
        }
    }
    clientSearching {
        ranking {
            type  "scalableFixed"
            value 2000
            priority 1
        }
        score {
            type "scalableFixed"
            value 1000
            priority 1
        }
    }
}
clients.add(directorBuilder.toString())

Collections.shuffle(clients)

for(int i = 0; i < clients.size(); i++) {
    String filename = String.format("client%d.json", i)
    Files.writeFile(clients.get(i), new File(String.format("%s/%s", directory, filename)))
}

def generateExtra() {
    Random rand = new Random();
    randomRankingValue = rand.nextInt(190) + 10; // 10 to 200
    randomScoreValue = rand.nextInt(290) + 10 // 10 to 300

    def builder = new JsonBuilder()
    def json = builder {
        clientSelf {
            ranking {
                type "nonScalableFixed"
                value 2000+randomRankingValue
            }
            score  {
                type "nonScalableFixed"
                value 1000+randomScoreValue
            }
        }
        clientSearching {
            ranking {
                type  "scalableRanged"
                lower 100
                upper 200
                priority 1
            }
            score {
                type "scalableRanged"
                lower 100
                upper 200
                priority 1
            }
        }
    }

    return builder.toString();
}


