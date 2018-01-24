import groovy.json.JsonBuilder
import org.testng.reporters.Files

String mainDirectory = "../src/integration-test/resources/validation"
String validDirectory = "valid"
String invalidByParameterNumberDirectory = "invalid_param_number"
String invalidByParameterValuesDirectory = "invalid_param_values"

List<String> validJsons = generateValidClients(30);
List<String> invalidByParameterNumberJsons = generateInvalidByParamNumberClients(30)
List<String> invalidByParameterValuesJsons= generateInvalidByParamValuesClients(30)

for(int i = 0; i < validJsons.size(); i++) {
    String filename = String.format("client%d.json", i)
    Files.writeFile(
            validJsons.get(i),
            new File(String.format("%s/%s/%s", mainDirectory, validDirectory, filename)))
}

for(int i = 0; i < invalidByParameterNumberJsons.size(); i++) {
    String filename = String.format("client%d.json", i)
    Files.writeFile(
            invalidByParameterNumberJsons.get(i),
            new File(String.format("%s/%s/%s", mainDirectory, invalidByParameterNumberDirectory, filename)))
}

for(int i = 0; i < invalidByParameterValuesJsons.size(); i++) {
    String filename = String.format("client%d.json", i)
    Files.writeFile(
            invalidByParameterValuesJsons.get(i),
            new File(String.format("%s/%s/%s", mainDirectory, invalidByParameterValuesDirectory, filename)))
}

def generateValidClients(int number) {

    List<String> generated = new ArrayList<>()

    for (int i = 0; i < number; i++) {
        Random rand = new Random();
        randomRankingValue = rand.nextInt(601) - 300; // -300 to 300
        randomScoreValue = rand.nextInt(401) - 200 // -200 to 200

        def builder = new JsonBuilder()
        def json = builder {
            clientSelf {
                ranking {
                    type "nonScalableFixed"
                    value 1000 + randomRankingValue
                }
                score {
                    type "nonScalableFixed"
                    value 500 + randomScoreValue
                }
            }
            clientSearching {
                ranking {
                    type "scalableRanged"
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
        generated.add(builder.toString())
    }

    return generated
}

def generateInvalidByParamNumberClients(int number) {

    List<String> generated = new ArrayList<>()

    for (int i = 0; i < number/2; i++) {
        Random rand = new Random();
        randomRankingValue = rand.nextInt(601) - 300; // -300 to 300
        randomScoreValue = rand.nextInt(401) - 200 // -200 to 200

        def builder = new JsonBuilder()
        def json = builder {
            clientSelf {
                ranking {
                    type "nonScalableFixed"
                    value 1000 + randomRankingValue
                }
                score {
                    type "nonScalableFixed"
                    value 500 + randomScoreValue
                }
                additional { // additional property
                    type "nonScalableFixed"
                    value 100
                }
            }
            clientSearching {
                ranking {
                    type "scalableRanged"
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
                additional {
                    type "nonScalableRanged"
                    lower 350
                    upper 450
                }
            }
        }
        generated.add(builder.toString())
    }
    for (int i = 0; i < number/2; i++) {
        Random rand = new Random();
        randomRankingValue = rand.nextInt(601) - 300; // -300 to 300
        randomScoreValue = rand.nextInt(401) - 200 // -200 to 200

        def builder = new JsonBuilder()
        def json = builder {
            clientSelf {
                ranking {
                    type "nonScalableFixed"
                    value 1000 + randomRankingValue
                } // missing property
            }
            clientSearching {
                ranking {
                    type "scalableRanged"
                    lower 700
                    upper 1300
                    priority 1
                }
            }
        }
        generated.add(builder.toString())
    }

    Collections.shuffle(generated)
    return generated
}

def generateInvalidByParamValuesClients(int number) {

    List<String> generated = new ArrayList<>()

    for (int i = 0; i < number/2; i++) {
        Random rand = new Random();
        randomRankingValue = rand.nextInt(601) - 300; // -300 to 300
        randomScoreValue = rand.nextInt(401) - 200 // -200 to 200

        def builder = new JsonBuilder()
        def json = builder {
            clientSelf {
                ranking {
                    type "nonScalableFixed"
                    value 1000 + randomRankingValue
                }
                score {
                    type "nonScalableFixed"
                    value 500 + randomScoreValue
                }
            }
            clientSearching {
                ranking {
                    type "scalableRanged"
                    lower 700
                    upper 1300 // missing param
                }
                score {
                    type "scalableRanged"
                    lower 300
                    upper 700
                    priority 1
                }
            }
        }
        generated.add(builder.toString())
    }
    for (int i = 0; i < number/2; i++) {
        Random rand = new Random();
        randomRankingValue = rand.nextInt(601) - 300; // -300 to 300
        randomScoreValue = rand.nextInt(401) - 200 // -200 to 200

        def builder = new JsonBuilder()
        def json = builder {
            clientSelf {
                ranking {
                    type "nonScalableFixed"
                    value 1000 + randomRankingValue
                }
                score {
                    type "nonScalableFixed"
                    value 500 + randomScoreValue
                }
            }
            clientSearching {
                ranking {
                    type "scalableRanged"
                    value randomRankingValue // incompatible param with type
                }
                score {
                    type "scalableRanged"
                    lower 300
                    upper 700
                    priority 1
                }
            }
        }
        generated.add(builder.toString())
    }

    Collections.shuffle(generated)
    return generated
}
