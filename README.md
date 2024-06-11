# Java-CoP

Welcome! This repo contains the Java-CoP: a connection method prover framework to abstract the proof-related algorithm for the user. It helps to focus only on literal representation, unification, and blocking strategies.

## Motivation

Implementing an Automated Theorem Prover (ATP) for classical or new logic is a hard task, especially for researchers who want to test or present their theoretical results in a simple approach.
One solution for developing provers is the Connection Method (CM), a goal-oriented direct proof search algorithm that looks for connections that unify a formula, allowing CM to perform competitively against the most prominent methods in ATP's field without low-level optimizations.
Another advantage of CM is its uniformity, allowing it to apply to a range of logical languages with the same proof technique.
Besides that, CM's uniformity provides a simple sharing of high-level optimization implementations between CMs.

## Installation

### Pre-requisites
- Java 17
- Maven

## How to install

Using your best command-line terminal, clone this repo and enter the root folder:
```bash
git clone git@github.com:renanlf/java-cop.git
cd java-cop
```
Then, run the following command to compile and create the jar file:
```bash
 mvn clean package assembly:single
```
If everything worked fine, a file called ```java-cop-$VERSION-jar-with-dependencies.jar``` was created in the `target/` folder.

## Running Java-CoP for the propositional case

Now, you can run the java-cop as the following example:
```bash
java -jar target/java-cop-$VERSION-jar-with-dependencies.jar -prover propositional -file $FILEPATH
```
Where `$FILEPATH` is a path to a CNF-valid file.
