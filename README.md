# coding_challenge

## How to run
CD into the root directory.
Compile the code with the following line: ``javac -cp "jar_files/*:."  *.java``

To verify the tests run the following line: ``java -cp "jar_files/*:." org.junit.runner.JUnitCore FlattenJsonTest``

To execute the actual code, run the following line: ``cat [pathToJsonFile] | java -cp "./jar_files/gson-2.8.6.jar:" FlattenJson``

An example would be: ``cat test.json | java -cp "./jar_files/gson-2.8.6.jar:" FlattenJson`` where test.json is the provided json-file located in the root directory.
To flatten other JsonObjects either modify the test.json file so it contains a Json of your own liking and run the same snippet or enter a valid path to another json-file.
