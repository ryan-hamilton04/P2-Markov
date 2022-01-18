# P3: Markov Part 2, Spring 2022

- [Project Introduction](#project-introduction)
    - [High-Level TODOs](#high-level-todos)
    - [Git](#git)
- [Markov Model and BaseMarkov Explained](#markov-model-and-basemarkov-explained)
    - [BaseMarkov](#basemarkov)
    - [getRandomText](#getrandomtext)
    - [getRandomText Example](#getrandomtext-example)
    - [Complexity of BaseMarkov.getRandomText](#complexity-of-basemarkovgetrandomtext)
- [Designing and Testing EfficientMarkov](#designing-and-testing-efficientmarkov)
    - [Background on EfficientMarkov](#background-on-efficientmarkov)
    - [The EfficientMarkov Class](#the-efficientmarkov-class)
    - [Testing EfficientMarkov](#testing-efficientmarkov)
- [Overview of Programming: WordMarkov](#overview-of-programming-wordmarkov)
    - [Implementing EfficientWordMarkov](#implementing-efficientwordmarkov)
- [Submitting, Analysis, Reflect](#submitting-analysis-reflect)
    - [Code](#code)
    - [Analysis](#analysis)
    - [Reflect](#reflect)
    - [Grading](#grading)
- [Appendix](#appendix)
    - [Assignment FAQ](#assignment-faq)
    - [Output of MarkovDriver](#output-of-markovdriver)
    - [What is a Markov Model?](#what-is-a-markov-model)
    - [Example Output of WordMarkov](#example-output-of-wordmarkov)


## Project Introduction
<details>
<summary>Click to Expand</summary>

The introduction to Markov Part 1 https://coursework.cs.duke.edu/201-public-documentation/P2-Markov-Part-1/-/blob/main/README.md  is appropriate to this project: generating random text using a Markov Model -- you should read that for background.

In this assignment, you'll be given a program that generates text using a Markov Model. You'll fork and clone the Git repository accessible via https://coursework.cs.duke.edu/201fall21/P3-markov-part2/-/tree/main. You'll do three things to complete this assignment:

1. Create a more efficient version of the `BaseMarkov` class by inheritance/extension. The new class is named `EfficientMarkov`. You're given a shell .java file for this class.
2. Create a similar more efficient class named `EfficientWordMarkov` of a provided program that uses words rather than characters. This program will leverage your `WordGram` class from Markov Part 1. You're given a working version of `WordGram` but no starter file for `EfficientWordMarkov`.
3. Develop and run benchmark tests comparing the base, inefficient class with the more efficient class you developed. You'll answer questions based on the benchmarks you run. Answer these questions and submit to Gradescope as a separate analysis assignment for P3: Efficient Markov Analysis.
</details>

## High Level TODOS
<details>
<summary>Click to Expand</summary>

Fork and clone the project. Then run the `MarkovDriver` program and verify its output matches the output given below. Then run the JUnit tests in `MarkovTest`, adding the JUnit library to your project if necessary.

Read about the differences between Base and Efficient methods, these differences are described below and manifest in different implementations of two methods: `setTraining` and `getFollows`. Understand the differences at a high-level and then implement these two methods as part of creating the class `EfficientMarkov`. Run `MarkovDriver` and `MarkovTest` after modifying these to use and test your `EfficientMarkov` class.

Implement the class `EfficientWordMarkov`, modeling it on `BaseWordMarkov` and making an internal map similar to `EfficientMarkov` but using `WordGram` objects as keys rather than strings. Test this implementation by running `MarkovDriver` modifying it to use your `WordGram` markov generating programs.

Submit code to Gradescope. Complete analysis and submit that to Gradescope.
</details>

## Git
<details>
<summary>Click To Expand</summary>

The URL for the code for the coding part of this assignment is: https://coursework.cs.duke.edu/201fall21/P3-markov-part2/-/tree/main. 

Fork, clone, and import the cloned project from the file system. **Be sure to fork first.** Then clone using the SSH URL after using a terminal window to `cd` into your IntelliJ workspace. 

When you make a series of changes you want to 'save', you'll push those changes to your GitLab repository. You should do this after major changes, certainly every hour or so of coding. You'll need to use the standard Git sequence to commit and push to GitLab:

```bash
git add .
git commit -m 'a short description of your commit here'
git push
```
</details>

## Markov Model and BaseMarkov Explained
<details>
<summary>Click To Expand</summary>


You'll create a more efficient version of the class `BaseMarkov` that generates random text using a Markov Model; the new class is named `EfficientMarkov`. You'll need to understand what a Markov Model is, how the `BaseMarkov` class works, and the ideas behind how to create the class `EfficientMarkov`. Your task in this part of the assignment is to create this more efficient class, verify that it works the same as the inefficient `BaseMarkov` class, and analyze the performance using a benchmarking program. To do this you'll need to understand how `BaseMarkov` works, how to make it more efficient using maps, and how the benchmarking program leverages inheritance and interfaces to run.

An order-k Markov model uses strings of k-letters to predict text, these are called *k-grams*. It's also possible to use k-grams that are composed of words rather than letters. An order-2 Markov model uses two-character strings or *bigrams* to calculate probabilities in generating random letters. A string called the *training text* is used to calculate these probabilities. For more on this model, see the [appendix](#appendix), or see the example in the next section.


### BaseMarkov
<details>
<summary>Click To Expand</summary>

`BaseMarkov` provides simple implementations of the methods defined in the interface `MarkovInterface`. The important methods that will change to make the class more efficient are `setTraining` and `getFollows`. You'll `@Override` these methods when creating `EfficientMarkov`, but otherwise rely on inherited methods from `BaseMarkov`.
</details>

### getRandomText
<details>
<summary>Click To Expand</summary>


The method we want to optimize to be more efficient is method `getRandomText()`. At a high level, the method works as follows (the exact code for `BaseMarkov.getRandomText()` is provided below). 

- Choose a random substring of `k` characters `current` from the text of length *N*:
- Repeat the following sequence of steps (with a `for` loop) up to the desired length *T*:
    - Get a list of all the characters in the training text that follow `current`
    - Choose one of those characters at random
    - If this character is `PSEUDO_EOS`
        - stop generating new text and `break` out of the `for` loop.
    - Otherwise,
        - Take the last *k*-1 characters of `current` and append this character onto the end of `current`.


</details>

### getRandomText Example
<details>
<summary>Click To Expand</summary>

Here is an example of how the algorithm works. Suppose we're using an order 3-gram (i.e., k=3) and the training text for generating characters is 

***"then five of these themes were themes were thematic in my theatre"***

Then the algorithm would proceed as follows:

- Choose a 3-letter substring from the text at random as the starting current 3-gram. This happens in method `BaseMarkov.getRandomText()` before the for-loop.
- Suppose this text referenced in `current` on line 56 is "the". These three characters are the start of the random text generated by the Markov model. In the loop above, the method `.getFollows(current)` is called on line 60. This method returns a list of all the characters that follow `current`, or "the", in the training text. This is `{"n", "s", "m", "m", “m", "a"}` in the text above -- look for each occurrence of "the" and see the character that follows to understand this returned list.
- One of these characters is chosen at random, is appended to `sb` as part of the randomly generated text, and then `current` is changed to drop the first character and add the last character. So if `"m"` is chosen at random (and it's more likely to be since there are two m's) the String `current` becomes `"hem"`.
- The loop iterates again and `"hem"` is passed to `getFollows()` -- the returned list will be `{"e", "e", "a"}`. The process continues until the designated number of random characters has been generated.
- In the example above if the string `"tre"` is chosen as the initial value of `current`, or becomes the value of `current` as text is generated, then there is no character that follows. In this case `getFollows("tre")` would return `{PSEUDO_EOS}` where the character `PSEUDO_EOS` is defined as the empty string. The loop in `getRandomText` breaks when `PSEUDO_EOS` is found -- ***this is an edge case and the designated number of random characters will not be generated.***

</details>

### Complexity of BaseMarkov.getRandomText
<details>
<summary>Click To Expand</summary>


As explained in the previous section, generating each random character requires scanning the entire training text to find the following characters when `getFollows` is called. Generating `T` random characters will call `getFollows` `T` times. Each time the entire text is scanned. If the text contains `N` characters, then generating `T` characters from a training text of size `N` is an O(`NT`) operation - meaning that the running time scales with the product of `N` and `T`.
</details>

</details>

## Designing and Testing EfficientMarkov
<details>
<summary>Click To Expand</summary>

You'll need to design, develop, test, and benchmark the new class. You'll create this class and make it extend `BaseMarkov` thus inheriting all its methods and protected instance variables. You'll need to create two constructors, see `BaseMarkov` for details. You'll inherit all the methods of `BaseMarkov` and you'll need to `@Override` two of them: `setTraining` and `getFollows` as described below.  The other methods and instance variables are simply inherited.

### Background on EfficientMarkov
<details>
<summary>Click To Expand</summary>

Calling `BaseMarkov.getFollows` requires looping over the training text of size *N*. In the class `EfficientMarkov`, you'll improve the efficiency of `EfficientMarkov.getFollows` by making it a constant time operation. In order to accomplish this, you'll need to create and initialize a `HashMap` instance variable used in `getFollows`. 

This means that in `EfficientMarkov`, we will scan through the training text of size *N* once before generating *T* random characters by calling `getFollows` *T* times.This makes `EfficientMarkov` text generation an O(*N*+*T*) operation instead of the O(*NT*) for `BaseMarkov` - that is, the running time scales with the sum of *N* and *T* instead of the product. We call this linear growth instead of quadratic.

Instead of rescanning the entire text of *N* characters as in `BaseMarkov`, you'll write code to store each unique k-gram as a key in the instance variable `myMap`, with the characters/single-char strings that follow the k-gram in a list associated as the value of the key. This will be done in the overridden method `EfficientMarkov.setTraining`. In the constructor, you'll create an instance variable `myMap` and fill it with keys and values in the method `EfficientMarkov.setTraining`.

**The keys in `myMap` are k-grams in a k-order Markov model**. Suppose we're creating an order-3 Markov Model and the training text is the string `"bbbabbabbbbaba"`. Each different k-gram in the training text will be a key in the map (e.g. `"bbb"`). **The value associated with each k-gram key is a list of single-character strings that follow the key in the training text (e.g. {`"a"`, `"a"`, `"b"`})**.  *Your map will have Strings as keys and each key will have an `ArrayList<String>` as the corresponding value.*

Let’s consider other 3-grams in the training text. The 3-letter string `"bba"` occurs three times, each time followed by `'b'`. The 3-letter string `"bab"` occurs three times, followed twice by `'b'` and once by `'a'`. 

What about the 3-letter string `"aba"`? It only occurs once, and it occurs at the end of the string, and thus is not followed by any characters. So, if our 3-gram is ever `"aba"` we will always end the text with that 3-gram. Suppose instead, there is one instance of `"aba"` followed by a `'b'` and another instance at the end of the text, then if our current 3-gram was `"aba"` we would have a 50% chance of ending the generation of random text early.

To represent this special case in our structure, we say that `"aba"` here is followed by an end-of-string (EOS) character. This isn't a real character, but a special String/character we'll use to indicate that the process of generating text is over.***While generating text, if we randomly choose the end-of-string character to be our next character, then instead of actually adding a character to our text, we simply stop generating new text and return whatever text we currently have.*** For this assignment, to represent an end-of-string character you'll use the static constant `PSEUDO_EOS` – see `MarkovModel.getRandomText` method for how this constant is used when generating random text.

</details>


### Constructors in EfficientMarkov
<details>
<summary>Click To Expand</summary>

One constructor has the order of the markov model as a parameter and the other,default constructor calls `this(3)` to set the order to three. The parameterized constructor you write will first call `super(order)` to initialize inherited state --- you'll then initialize the instance variable `myMap` to a `HashMap`. 

</details>

### Building myMap in setTraining
<details>
<summary>Click To Expand</summary>

You must set `myText` to the parameter text as the first line in your new `setTraining` implementation. You can do this directly, or by calling `super.setTraining(text)`.

For `getFollows` to function correctly, even the first time it is called, you'll clear and initialize the map when the overridden method `setTraining` method is called. At the beginning of your method, after setting the value of `myText`, write `myMap.clear()` to accomplish this.

Implement the method according to the background. As a refresher, here are the list of steps you need to complete:

The map will be constructed in the parameterized constructor and keys/values added in this method. To continue with the previous example, suppose we're creating an order-3 Markov Model and the training text is the string `"bbbabbabbbbaba"`.

| Key | List of Values |
| ---- | ------        |
| `"bbb"` | `{"a", "b", "a"}` |
| `"bba"` | `{"b", "b", "b"}` |
| `"bab"` | `{"b", "b", "a"}` |
| `"abb"` | `{"a", "b"}` |
| `"aba"` | `{PSEUDO_EOS}` |


In processing the training text from left-to-right, e.g., in the method `setTraining`, we see the following 3-grams starting with the left-most 3-gram `“bbb”`. Your code will need to generate every 3-gram in the text as a possible key in the map you'll build. Use the `String.substring()` method to create substrings of the appropriate length, i.e., `myOrder`. In this example the keys/Strings you'll generate are:

`bbb` -> `bba` -> `bab` -> `abb` -> `bba` -> `bab` -> `abb` -> `bbb` -> `bbb` -> `bba` -> `bab` -> `aba`

You'll create these using `myText.substring(index, index+myOrder)` if `index` is accessing all valid indices.

As you create these keys, you'll store them in the map and add each of the following single-character strings to the ArrayList value associated with the 3-gram key.

For example, you'd expect to see these keys and values for the string `"bbbabbabbbbaba"`. The order of the keys in the map isn't known, but for each key the order of the single-character strings should be as shown below -- the order in which they occur in the training text.

</details>

### Method getFollows in EfficientMarkov
<details>
<summary>Click to Expand</summary>

This method simply looks up the key in a map and returns the associated value: an `ArrayList` of single-character strings that was created when `setTraining` is called. If the key isn't in the map you should throw an exception, e.g., 

`throw new NoSuchElementException(key+" not in map");`

The code in this version of `getFollows` is constant time because if the key is in the map, the corresponding value is simply returned. The value for each key is set in the method `setTraining`.

</details>

### Testing EfficientMarkov
<details>
<summary>Click to Expand</summary>

To test that your code is doing things faster and not differently, you can use the same text file and the same order *k* for k-grams for `EfficientMarkov` model. Simply use an `EfficientMarko`v object rather than a `BaseMarkov` object when running `MarkovDriver`. 

***If you use the same seed in constructing the random number generator used in your new implementation, you should get the same text, but your code should be faster.*** You can use `MarkovDriver` to test this. Do not change the given random seed when testing the program, though you can change it when you'd like to see more humorous and different random text. You can change the seed when you're running the program, **but for testing and for submitting you should use the provided seed 1234.**  

</details>



### JUnit for EfficientMarkov
<details>
<summary>Click to Expand</summary>

Use the JUnit tests in the `MarkovTest` class as part of testing your code. ***You will need to change the class being tested that's returned by the method `getModel`***. For discussion on using JUnit tests, see the [section in this document](https://coursework.cs.duke.edu/201-public-documentation/P2-Markov-Part-1/-/blob/main/README.md#junit-tests-explained) on how to run a Java program that uses JUnit tests. You may need to add JUnit 5 to the project -- you can do this by using option-enter and choosing that version of JUnit. On Windows machines use ALT-enter. Alternatively, right click any red text in IntelliJ relating to JUnit, click "Show Context Actions", and select the most recent version of JUnit.

</details>



### Debugging Your Code in EfficientMarkov
<details>
<summary>Click to Expand</summary>

It’s hard enough to debug code without random effects making it even harder. In the `BaseMarkov` class you’re provided, the Random object used for random-number generation is constructed as follows:

`myRandom = new Random(RANDOM_SEED);`

`RANDOM_SEED` is defined to be 1234. Using the same seed to initialize the random number stream ensures that the same random numbers are generated each time you run the program. Removing `RANDOM_SEED` and using `new Random()` will result in a different set of random numbers, and thus different text, being generated each time you run the program. This is more amusing, but harder to debug. ***If you use a seed of `RANDOM_SEED` in your `EfficientMarkov` model, you should get the same random text as when the brute-force method from `BaseMarkov` is used.*** This will help you debug your program because you can check your results with those of the code you’re given which you can rely on as being correct. You'll get this behavior "for free" since the first line of your `EfficientMarkov` constructor will be `super(order)` -- which initializes the `myRandom` instance variable.

</details>

</details>

## Overview of Programming: WordMarkov
<details>
<summary>Click to Expand</summary>

If you change the `MarkovDriver` to use a `BaseWordMarkov` class instead of a `BaseMarkov` class then words rather than characters will be used to generate a model. You'll need a working `WordGram` class from the [Part 1 Markov Assignment](https://coursework.cs.duke.edu/201fall21/P2-Markov-Part-1). *One is provided for you to use as part of the code you clone from the Git repository*. Text generated for 50 words is shown in the [appendix](#appendix). Here a k-gram is a sequence of *k* words, e.g., a `WordGram` rather than a String of k-characters. You can generate this using `MarkovDriver` and the `BaseWordMarkov` object in that class.

Just as you created `EfficientMarkov` by extending `BaseMarkov`, you'll create `EfficientWordMarkov` by extending `BaseWordMarkov`. You'll create two constructors and implement two methods similar to those in `EfficientMarkov`: `setTraining` and `getFollows`. The difference is that instead of String objects as keys in a map you'll be using `WordGram` objects as keys. You must create the `EfficientWordMarkov` class from scratch; you're not provided with starter code.

### Implementing EfficientWordMarkov
<details>
<summary>Click to Expand</summary>

You'll model this class on the `EfficientMarkov` class you've already implemented and tested. See the previous section for details. However in this version you will use `WordGram` objects as keys in a map and the instance variable String `myText` from `BaseMarkov` becomes `String[] myWords` in `BaseWordMarkov`. 

You'll create two constructors in `EfficientWordMarkov`: `public EfficientWordMarkov()` and `public EfficientWordMarkov(int order)`. These constructors should be identical to those in `EfficientMarkov` (default order 3, initialize myMap to be a new HashMap).

You'll need to use the code in `BaseWordMarkov` to help reason about how to write `setTraining` in `EfficientWordMarkov`. The `EfficientWordMarkov.getFollows` method is the same as in `EfficientMarkov`, though `myMap` is different. Now it's `Map<WordGram, ArrayList<String>>` since words are used rather than characters. You'll need to reason how to create the map and initialize its contents in `setTraining`.

***In creating an array of words, you should use `text.split("\\s+")` to process the String passed to `setTraining` into an array of "words" separated by whitespace. You'll see this code in `BaseWordMarkov`.***

Some hints about `EfficientWordMarkov` compared with `EfficientMarkov`:
- Instance variable is `myWords` rather than `myText`, see `BaseWordMarkov` for details.
- Instead of using `String.substring()` to create a String for every key, you'll create a new `WordGram` for every key in the map.
- Instead of using a one-character `String` to follow each key, you'll use the appropriate `String` in `myWords` as the string that follows each key.
- In method `getRandomText` you can call the `shiftAdd` method after finding a following word (by calling `getFollows`). Recall that `shiftAdd` creates a new `WordGram` object, you'll use this as the key for generating the next word at random.

To test your class, use it in the `MarkovDriver` program and compare the output to what's generated by `BaseWordMarkov` just as you did when [Testing `EfficientMarkov`](#testing-efficientmarkov).
</details>

</details>

## Submitting, Analysis, Reflect 
<details>
<summary>Click to Expand</summary>

### Code
Push your code to Git. Do this often. You can use the autograder on Gradescope to test your code. You should NOT complete the reflect form until you're done with all the coding portion of the assignment. Since you may uncover bugs from the autograder, you should wait until you've completed debugging and coding before completing the reflect form.

### Analysis
<details>
<summary>Click to Expand</summary>

You're given a class `Benchmark` that runs several tests that allow you to compare the performance (in terms of the program runtime) of the default, brute-force `BaseMarkov` and the more efficient map-based `EfficientMarkov` code. The code you start with uses `data/hawthorne.txt`, which is the text of ***A Scarlet Letter***, a text of 487,614 characters (as you'll see in the output when running the benchmark tests). The class uses `BaseMarkov`, but can be easily changed to use `EfficientMarkov` by changing the appropriate line in the `getMarkov` method called from main. You're free to alter this class.

Answer the following questions in your analysis. You'll submit your analysis as a separate PDF as a separate assignment ***to Gradescope***. Note that we are not looking for particular numbers so much as reasonable interpretations of your results.

1. *Change made on 5:30 PM on Sept 25: Use provided data rather than run your own*. 

Use this data for `BaseMarkov` run on staff laptop via the `BenchMark` program using the default file and an order 5 Markov Model.
|time |   source | #chars |
|------|--------|------|
|0.107   |487614  |1000 |
|0.204   |487614  |2000 |
|0.302   |487614  |8000 |
|1.440   |487614  |16000 |
|2.825   |487614  |32000 |
|5.674   |487614  |64000 |

The program also generates 4,096 characters using texts that increase in size from 487,614 characters to 4,876,140 characters (10 times the number).  This data is shown below.

|time |   source | #chars |
|------|--------|------|
|0.370   |487614  |4096 |
|0.720   |975228  |4096 |
|1.065  |1462842 |4096 |
|1.421   |1950456 |4096 |
|1.795   |2438070 |4096 |
|2.121   |2925684 |4096 |
|2.531   |3413298 |4096 |


In your analysis file, include an explanation as to whether the timings support the O(*NT*) quadratic growth analysis. That is, do your timings suggest that the running time scales with the product of the size of the training text *N* and the number of characters to be generated *T*. Use the fact that for some runs *N* is fixed and *T* varies whereas in the other runs *T* is fixed and *N* varies.

2. Determine (from running `Benchmark.java`) how long it takes for `EfficientMarkov` to generate 2,000, 4,000, 8,000, 16,000, and 32,000 random characters using the default file and an order 5 Markov Model. Include these timings in your report. The program also generates 4,096 characters using texts that increase in size from 487,614 characters to 4,876,140 characters (10 times the number). In your analysis file include an explanation as to whether the timings support the O(*N*+*T*) analysis. That is, do your timings suggest that the running time scales with the sum of the size of the training text *N* and the number of characters to be generated *T*.

3. Read the article *Can’t Access GPT-3? Here’s GPT-J — Its Open-Source Cousin* accessible via this link: https://towardsdatascience.com/cant-access-gpt-3-here-s-gpt-j-its-open-source-cousin-8af86a638b11 . Describe any thoughts you have about the article as it relates to this assignment. 

***After completing the analysis questions you submit your answers in a PDF to Gradescope in the appropriate assignment.***

</details>

### Reflect

Answer questions in this form: https://docs.google.com/forms/d/e/1FAIpQLSfsEbHxVJSP3GwxTGf17FaS8FjAvzb9cyhpfKwBouKTsDVV8Q/viewform 

### Grading

For this program grading will be:

| Points | Grading Criteria |
| ------ | ------ |
| 16 | Correctness of EfficientMarkov and EfficientWordMarkov code. (10 for EfficientMarkov, 5 for EfficientWordMarkov, 1 for API)|
| 6 | Answers to analysis questions |
| 2 | Reflect form |

</details>

## Appendix
<details>
<summary>Click to Expand</summary>

### Assignment FAQ

**My unchanged `BaseMarkov` does not produce the same output as reported above or does not give the correct number of characters as described in `analysis.txt`.** 

Post to Ed. This is unlikely to happen for correct programs.

**When I run `Benchmark` using `EfficientMarkov`, it takes even longer than `BaseMarkov` and/or generates an `OutOfMemoryException`.**

Make sure you clear the map at the start of `setTraining` by calling `myMap.clear()`. Because the `Benchmark` class creates a single `EfficientMarkov` object and calls `setTraining()` several times, if you do not clear the map, new values will be added every time the method is called.

**How can I debug my `EfficientMarkov` implementation?**

You can test your implementation by providing a String like `“bbbabbabbbbaba”` as the training text and print the keys and values of the map you build to confirm that your map is constructed correctly. Working out simple cases by hand and confirming them with your code is a good way to test code in general.
In order to run your code, you can make some changes to the `main()` method in `MarkovDriver` or `Benchmark`.

**How do I deal with randomness?**

It’s hard enough to debug code without random effects making it even harder. In the `MarkovModel` class you’re provided, the Random object used for random-number generation is constructed as follows:

`myRandom = new Random(RANDOM_SEED);`

`RANDOM_SEED` is defined to be 1234. Using the same seed to initialize the random number stream ensures that the same random numbers are generated each time you run the program. Removing `RANDOM_SEED` and using `new Random()` will result in a different set of random numbers, and thus different text, being generated each time you run the program. This is more amusing, but harder to debug. If you use a seed of `RANDOM_SEED` in your `EfficientMarkov` model you should get the same random text as when the brute-force method is used. This will help you debug your program because you can check your results with those of the code you’re given which you can rely on as being correct. You'll get this behavior "for free" since the first line of your `EfficientMarkov` constructor will be `super(order)` -- which initializes the `myRandom` instance variable.

If you use the same `RANDOM_SEED` in constructing the random number generator used in your new implementation, you should get the same text, but your code should be faster. You can use `MarkovDriver` to test this. Do not change the given `RANDOM_SEED` random seed when testing and submitting the program, though you can change it when you'd like to see more humorous and different random text.

**The length of the Markov Model is way too small**
 
The code is encountering the EOS tag too soon and then exiting - look over where you’re adding the EOS tag.

### Output of MarkovDriver
<details>
<summary>Click to Expand</summary>

This table shows the output of different Markov Models for President Trump's State of the Union address in 2017 and President Bush's State of the Union address in 2007. 

| k | President Trump State of the Union 2017 | President Biden State of the Union 2021 |
| - | ------                                  | ------                                 |
| 1 |  t bers, thatetiourotha atr Itry es tr l en thed mpparow eves, The l t. tos tive derke, t d, by. Ames ther ais, outyeneanonerod pisind Th. pr cres olinacrop a chencon borurenge ind on thers thest wat w | r bus io vear mans anovine se futinybereagicorthences – min pl anginsethec. lid Imint a me e estoreon We alel the tho Wed orreshilthe- lrien thaionveost NIDandend jon cempest Amaid ofit’r thnkege awis |
| 2 | ps of Yort sameriabonuchismany to reving theiregive nor toget ou, war. Shat ve vinno Nat thar deart ree.” Alatiall ar And on Natiens, to plese — and on, an eve imboyme shly offely, as penigh Kengrear| om of rovestin thater the meris to becaust Chilly th. It’s itiand Amerear precouleassarms on Just it usell cour con 9/11.3 muld st an ton. Stax owthat to come so puble coles divered magaild ouggle vi|
| 3 | f us the pracificity othe including. Pare workers of his Capitol, meets with in againspire food there need. Aftere in our strain. We regislast, homento uneminists, them, thinally are top prover our Go | irst on pock of their a Medica’s demong Right ten andame failesses at I know – it we cans by bet why time to said by more imple. We ass the wrong the nation a your drugs. Tonighternet, and in Famillio |
| 4 | arriorities why, this like Congressure the nearly 400, incoln an incredible to hardships are build our countain minor Otto join me it, but where break to than $4,000 — so Americans — manufacture of St | life can will crising the faucet again. The world. Talk away. So how right — and get by negotiating than 400,000 pharmacism to be big tax brack up. That I’m provided to act police President have act. |
| 5 | has taughters order Patrol Agents, and together, gaining our difficult — because of the authority and that we do. I will be a major plants will determined a tube to endured by criminals and minor chi | permanent study shot an option during the productive diseases opens without our high-speed international to Americans can be first 100 Days of joy, cried out the central challenges facing nothing up. |
</details>

### What is a Markov Model?
<details>
<summary>Click to Expand</summary>

An order-k Markov model uses strings of k-letters to predict text: these are called *k-grams*. It's also possible to use k-grams that are composed of words rather than letters. An order-2 Markov model uses two-character strings or *bigrams* to calculate probabilities in generating random letters. A string called the *training text* is used to calculate these probabilities.

For example, suppose that in the text we're using for generating random letters, the so-called training text, using an order-2 Markov model, the bigram `"th"` is followed 50 times by the letter `"e"`, 20 times by the letter `"a"`, and 30 times by the letter `"o"`, because the sequences `"the"`, `"tha"`, and `"tho"` occur 50, 20, and 30 times, respectively while there are no other occurrences of `"th"` in the text we're modeling.

Now suppose that in generating random text using an order-2 Markov process we generate the bigram `"th"` --- then based on this bigram we must generate the next random character using the order-2 model. The next letter will be an 'e' with a probability of 0.5 (50/100); will be an 'a' with probability 0.2 (20/100); and will be an 'o' with probability 0.3 (30/100). If 'e' is chosen, then the next bigram used to calculate random letters will be `"he"` since the last part of the old bigram is combined with the new letter to create the next bigram used in the Markov process.

Rather than using probabilities explicitly, your code will use them implicitly. You'll store 50 occurrences of `"e"`, 20 occurrences of `"a"` and 30 occurrences of `"o"` in an `ArrayList`. You'll then choose one of these at random. This will replicate the probabilities, e.g., of 0.3 for `"o"` since there will be 30 `"o"` strings in the 100-element `ArrayList`.
</details>

### Example Output of WordMarkov
<details>
<summary>Click to Expand</summary>

| k | President Trump State of the Union 2017 | President Biden State of the Union 2021 |
| - | ------                                  | ------                                 |
| 1 | friends on Long Island. His tormentors wanted to reform is moving a train tracks, exhausted from Mexico to reopen an order directing Secretary Mattis to independence, and a mountain, we are dreamers too. Here tonight — and paramedics who will see their corrupt dictatorship, I am asking both parties as | the 21st Century. We cannot walk away from their net worth increase by man—made and save lives. And we will long endure is familiar, this podium, and I know the American households. We’ve done nothing – Democrats and billionaires who knows what it’s in that have to leave. Our grids|
| 2 | expected, and others we could never have imagined. We have faced challenges we expected, and others we could never have imagined.  We have faced challenges we expected, and others we could never have imagined. We have shared in the gallery with Melania. Ashlee was aboard one of our country. The | survive. It did. But the struggle is far from over. The questionof whether our democracy since the Great Depression. The worst economic crisis since the Great Depression. The worst pandemic in a direct and proportionate way to Russia’s interference in our mutual interests. As we gather here tonight, the|
| 3 | respect our country. The fourth and final pillar protects the nuclear family by ending chain migration. Under the current broken system, a single immigrant can bring in virtually unlimited numbers of distant relatives. Under our plan, those who meet education and work requirements, and show good moral character, will be | we need to ensure greater equity and opportunity for women. Let’s get the Paycheck Fairness Act to my desk as soon as possible. I also hope Congress can get to my desk the Equality Act to protect Asian Americans and Pacific Islanders from the vicious hate crimes we’ve seen this |
| 4 | we passed tax cuts, roughly 3 million workers have already gotten tax cut bonuses — many of them thousands of dollars per worker.  Apple has just announced it plans to invest a total of $350 billion in America, and hire another 20,000 workers. This is our new American moment. There | our tables. Immigrants have done so much for America during the pandemic – as they have throughout our history. The country supports immigration reform. Congress should act. We have a giant opportunity to bend to the arc of the moral universe toward justice. Real justice. And with the plans I|
| 5 | did not stay silent. America stands with the people of Iran in their courageous struggle for freedom. I am asking the Congress to end the dangerous defense sequester and fully fund our great military. As part of our defense, we must modernize and rebuild our nuclear arsenal, hopefully never having | soul of America – we need to protect the sacred right to vote. More people voted in the last presidential election than ever before in our history – in the middle of one of the worst pandemics ever. That should be celebrated. Instead it’s being attacked. Congress should pass H.R.|

</details>
