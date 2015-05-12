# Usage

## Maven dependencies

Add Maven dependencies to your pom.xml:

```xml
<dependency>
    <groupId>com.protonail.leveldb-jna</groupId>
    <artifactId>leveldb-jna-core</artifactId>
    <version>x.x.x</version>
</dependency>

<dependency>
    <groupId>com.protonail.leveldb-jna</groupId>
    <artifactId>leveldb-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>osx</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.leveldb-jna</groupId>
    <artifactId>leveldb-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>windows-x86_64</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.leveldb-jna</groupId>
    <artifactId>leveldb-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>windows-x86</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.leveldb-jna</groupId>
    <artifactId>leveldb-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>linux-x86_64</classifier>
</dependency>
<dependency>
    <groupId>com.protonail.leveldb-jna</groupId>
    <artifactId>leveldb-jna-native</artifactId>
    <version>x.x.x</version>
    <classifier>linux-x86</classifier>
</dependency>
```

If you don't want to use native libraries for some platforms (see `classifier` tag) then just remove theirs dependencies.

If you want to use snapshot version (with `-SNAPSHOR` suffix) then just add snapshot repository to your pom.xml:

```xml
<repositories>
    ...
    <repository>
        <id>sonatype</id>
        <url>https://oss.sonatype.org/content/groups/public</url>
    </repository>
    ...
</repositories>
```

## Create LevelDB database

```java
try(LevelDBOptions options = new LevelDBOptions()) {
    options.setCreateIfMissing(true);

    try(LevelDB levelDB = new LevelDB(databaseDirectory, options)) {
        // Work with database here
    }
}

```

## Open LevelDB database

```java
try(LevelDBOptions options = new LevelDBOptions()) {
    try(LevelDB levelDB = new LevelDB(databaseDirectory, options)) {
        // Work with database here
    }
}

```

## Get value by key

```java
try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
    byte[] value = levelDB.get(key, readOptions);
}
```

## Put value by key

```java
try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
    levelDB.put(key, value, writeOptions);
}
```

## Delete value by key

```java
try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
    levelDB.delete(key, writeOptions);
}
```

## Write batch

```java
try(LevelDBWriteOptions writeOptions = new LevelDBWriteOptions()) {
    try(LevelDBWriteBatch writeBatch = new LevelDBWriteBatch()) {
        writeBatch.delete(keyForDelete);
        writeBatch.put(keyForPut, valueForPut);

        levelDB.write(writeBatch, writeOptions);
    }
}
```

## Iterate by keys

```java
try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
    try(LevelDBKeyIterator iterator = new LevelDBKeyIterator(levelDB, readOptions)) {
        while (iterator.hasNext()) {
            byte[] currentKey = iterator.next();

            // Work with key
        }
    }
}
```

## Iterate by keys and values

```java
try(LevelDBReadOptions readOptions = new LevelDBReadOptions()) {
    try(LevelDBKeyValueIterator iterator = new LevelDBKeyValueIterator(levelDB, readOptions)) {
        while (iterator.hasNext()) {
            KeyValuePair pair = iterator.next();

            byte[] currentKey = pair.getKey();
            byte[] currentValue = pair.getValue();

            // Work with key and value
        }
    }
}
```

## Get property

```java
String propertyValue = levelDB.property("leveldb.stats");
```

## Get approximate sizes

```java
Range range1 = new Range("a".getBytes(), "k00000000000000010000".getBytes());
Range range2 = new Range("k00000000000000010000".getBytes(), "z".getBytes());

long[] sizes = levelDB.approximateSizes(range1, range2);
```

## Compact range

```java
levelDB.compactRange(fromKey, toKey);
```

## Repair database

```java
try(LevelDBOptions options = new LevelDBOptions()) {
    LevelDB.repair(databaseDirectory, options);
}
```

## Destroy database

```java
try(LevelDBOptions options = new LevelDBOptions()) {
    LevelDB.destroy(databaseDirectory, options);
}
```
