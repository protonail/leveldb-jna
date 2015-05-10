# Testing

## How to run test?

```
mvn test
```

## How to run specific test only?

```
mvn test -Dtest=LevelDBTest#open_database_twice -DfailIfNoTests=false
```

More examples [here](http://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html).