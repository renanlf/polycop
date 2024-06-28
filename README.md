# Java-CoP

Welcome! This repo contains the Java-CoP: a connection method prover framework to abstract the proof-related algorithm for the user. It helps to focus only on literal representation, unification, and blocking strategies.

## Motivation

Implementing an Automated Theorem Prover (ATP) for classical or new logic is a hard task, especially for researchers who want to test or present their theoretical results in a simple approach.
One solution for developing provers is the Connection Method (CM). This goal-oriented direct proof search algorithm looks for connections that unify a formula. This allows CM to perform competitively against the most prominent methods in ATP's field without low-level optimizations.
Another advantage of CM is its uniformity, which allows it to be applied to a range of logical languages with the same proof technique.
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
 mvn clean package -DskipTests assembly:single
```
If everything worked fine, a file called ```java-cop-$VERSION-jar-with-dependencies.jar``` was created in the `target/` folder.

## Running Java-CoP for the propositional case

Now, you can run the java-cop as the following example:
```bash
java -jar target/java-cop-$VERSION-jar-with-dependencies.jar -prover propositional -file $FILEPATH
```
Where `$FILEPATH` is a path to a CNF-valid file.

## Generating proof in sequent-style to a .tex file

Another key feature of the Java-CoP command-line runner is the ability to generate a LaTeX (.tex) file with the proof in sequent style using the ```-latex $LATEX_FILEPATH``` argument. The file will have its content like the following:
```latex
\documentclass[convert={outext=.eps, command=\unexpanded{pdftops -eps \infile}}]{standalone}
\usepackage{bussproofs} % for sequent-style proofs
\begin{document}
\AxiomC{}
\RightLabel{Ax}
\UnaryInfC{$\{\}, M, [1]$}
\AxiomC{}
\RightLabel{Ax}
\UnaryInfC{$\{\}, M, [-2, -3]$}
\AxiomC{}
\RightLabel{Ax}
\UnaryInfC{$\{\}, M, [-2]$}
\RightLabel{\textit{Ext}}
\BinaryInfC{$[-3], M, [-2]$}

\AxiomC{}
\RightLabel{Ax}
\UnaryInfC{$\{\}, M, []$}
\RightLabel{\textit{Ext}}
\BinaryInfC{$[-2], M, []$}

\RightLabel{\textit{Ext}}
\BinaryInfC{$[1, -2], M, []$}

\RightLabel{\textit{St}}
\UnaryInfC{$\varepsilon, M, \varepsilon$}

\DisplayProof

\end{document}
```
The output of the above document in Overleaf is the following:
![image](https://github.com/renanlf/java-cop/assets/8339052/303ad47d-e01c-4056-98a3-fb57af1d5627)
