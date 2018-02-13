import groovy.json.JsonBuilder
import org.testng.reporters.Files

int numberOfGeneratedJsons = 100
String directory = "../src/integration-test/resources/all_matching_clients"

for(int i = 0; i < numberOfGeneratedJsons; i++) {
    String jsonBody = generateClient()
    String filename = String.format("client%d.json", i)
    Files.writeFile(jsonBody, new File(String.format("%s/%s", directory, filename)))
}

def generateClient() {
    Random rand = new Random()
    randomRankingValue = rand.nextInt(601) - 300 // -300 to 300
    randomScoreValue = rand.nextInt(401) - 200 // -200 to 200

    def builder = new JsonBuilder()
    def json = builder {
        clientSelf {
            ranking {
                type "nonScalableFixed"
                value 1000+randomRankingValue
            }
            score  {
                type "nonScalableFixed"
                value 500+randomScoreValue
            }
        }
        clientSearching {
            ranking {
                type  "scalableRanged"
                lower 700
                upper 1300
                priority 1
            }
            score {
                type "scalableRanged"
                lower 300
                upper 700
                priority 1
            }
        }
    }

    return builder.toString()
}

