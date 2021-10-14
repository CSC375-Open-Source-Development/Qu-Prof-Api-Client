# Quinnipiac Professor API Client

## What is this?

This repo provides a client API wrapper for grabbing information about Quinnipiac professors from a school site.

There is no official API for this -- I found the information I needed through looking at browser network calls.

It...works, that's all I'll say about it.

## How to use this?

You can look at the `Main.java` file (src/main/java/Main.java) for an example on how to use this code.

Creating the client object is easy!
```java
QuProfApiClient client = new QuProfApiClient();
```

From there, you can call any method on the client object to get professor data.

The `getProfessors` method takes in a page number and will return all professor information from that page.
The professors on each page number are based on the website's "API", I have no control over it.
```java
ArrayList<Professor> professors = client.getProfessors(1);
```

Check out the `Professor.java` class (src/main/java/responseObjects/Professor.java) to see what information is returned about each professor.

That's it -- things are pretty limited right now.