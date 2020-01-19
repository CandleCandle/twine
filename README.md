# Twine #

Generic library implementing a rope data structure.

## Usage ##

### Maven ###
Include a dependency in your `pom.xml`
```xml
<dependency>
    <groupId>uk.me.candle.twine</groupId>
    <artifactId>twine</artifactId>
    <version>[latest version]</version>
</dependency>
```
The latest version can be found on maven central: https://search.maven.org/search?q=g:uk.me.candle%20a:twine

### Java ###

```java
Rope<Character, CharSeqSliceable> rope = new BasicMutableRope<>();
// create a new empty Rope.

rope.append(new CharSeqSliceable("this is a rope element.\n"));
rope.append(new CharSeqSliceable("this is another rope element."));
// `rope` now contains "this is a rope element.\nthis is another rope element."

List<Character, CharSeqSliceable>> parts = rope.split(15);
// parts.get(0) now contains a Rope containing "this is a rope "
// parts.get(1) now contains a Rope containing " element.\nthis is another rope element."

Rope<Character, CharSeqSliceable> another = rope.insert(new CharSeqSliceable("\r"), 24);
// `another now contains "this is a rope element.\r\nthis is another rope element."
```

## Extending ##

Implement the `Sliceable` interface for your array/list-like data structure.
```java
public class MySliceable implements Sliceable<ElementType, MySliceable> { ... }
```
Create a `Rope` using it:
```java
Rope<ElementType, MySliceable> rope = new BasicMutableRope<>();
```
