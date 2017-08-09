This is a spring-boot based implementation of task. 

Once you type `mvn clean verify` it will execute all tests and build a runnable jar at `..\console-roulette-1.0.jar`.

Both basic and bonus parts of task are implemented.

Few points to mention:

1. If you want to customize file name with players then ads `--input` param, e.g. `java -jar console-roulette-1.0.jar --input="..\custom.txt"` or via IDE, otherwise `input.txt` file will be used from classpath.
2. There is a class with main method which you can use: `com.github.al.roulette.RouletteApplication`
3. Application accepts bet amounts both with and without floating point
4. There is a nice API to add new players to the game and to add new bets as well
5. Currently default system IO is used to accepts bets - this can be modified to any source if required (need to provide proper Iterator implementation only)