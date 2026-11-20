# Algorithm Visualization Tool

A Java Swing application for learning how sorting algorithms and data structures work through step-by-step visualizations.

## Current progress

- Welcome screen and visualization selection menu
- Console Playground page with text output and input support; prompts are written in `ConsolePlayground.java`
- Navigation to a placeholder screen for each planned topic
- Planned order follows the Lafore textbook: Bubble Sort, Selection Sort, Stack, Queue, Linked List, Merge Sort, and Quick Sort

## Run

Requires a JDK. From the repository root, run:

```sh
javac -d out src/avt/*.java
java -cp out avt.Main
```

The GUI and step-by-step demonstrations will be added incrementally.
