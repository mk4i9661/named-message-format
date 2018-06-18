# Description
A library that enables user to specify the name of a format group in a way similar to `java.text.MessageFormat`. In fact, the library utilizes `java.text.MessageFormat` class for the sake of simplicity.

# Usage
```java
Map<String, Object> parameters = new TreeMap<>();
parameters.put("age", 20);
parameters.put("name", "John");

assertThat(
        NamedMessageFormat.format(
                "Hello, my name is {name} and I am {age} years old.",
                parameters
        ),
        is("Hello, my name is John and I am 20 years old.")
);
```

# How to build
```bash
./gradlew jar
```