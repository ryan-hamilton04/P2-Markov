# Project 3: Markov Part 2, Spring 2022

This is the directions document for Project P2 Markov in CompSci 201 at Duke University, Fall 2022. [This document details the workflow](hhttps://coursework.cs.duke.edu/cs-201-fall-22/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.

## Outline

TODO

## Introduction

Random Markov processes are widely used in Computer Science and in analyzing different forms of data. This project offers an occasionally amusing look at a *generative model* for creating realistic looking text in a data-driven way. To do so, you will implement two classes: First `WordGram` which represents immutable sequences of words, then `HashMarkov` which will be an efficient model for generating random text that uses `WordGram`s and `HashMap`s.

Generative models of the sort you will build are of great interest to researchers in artificial intelligence and machine learning generally, and especially those in the field of *natural language processing* (the use of algorithmic and statistical AI/ML techniques on human language). One recent and powerful example of such text-generation model via statistical machine learning program is the [OpenAI GPT-3](https://openai.com/blog/gpt-3-apps/).


### What is a `WordGram`

You will implement a class called `WordGram` that represents a sequence of words (represented as strings), just like a Java String represents a sequence of characters. Just as the Java String class is an immutable sequence of characters, the `WordGram` class you implement will be an immutable sequence of strings. Immutable means that once a WordGram object has been created, it cannot be modified. You cannot change the contents of a `WordGram` object. However, you can create a new WordGram from an existing `WordGram`.

The number of strings contained in a `WordGram` is sometimes called the *order* of the WordGram, and we sometimes call the `WordGram` an *order-k* WordGram, or a *k-gram* -- the term used in the Markov program you'll implement for Part 2.  You can expand below to see some examples of order-3 `WordGram` objects.

<details>
<Summary>Expand to see examples of order-3 `WordGram`s</summary>

| | | |
| --- | --- | --- |
| "cat" | "sleeping" | "nearby" |
| | | |

and 
| | | |
| --- | --- | --- |
| "chocolate" | "doughnuts" | "explode" |
| | | |

</details> 

### What is a Markov Model?

TODO: Update to refer to `WordGram`s instead of characters

An order-k Markov model uses strings of k characters to predict text: we call these *k-grams*. An order-2 Markov model uses two-character strings or *bigrams* to calculate probabilities in generating random letters. A string called the *training text* is used to calculate these probabilities. We walk through an example calculating the probabilities in the expandable section below. One can also use k-grams that are composed of words rather than letters. We use `WordGram` objects to represent these, but the logic is the same as that shown in the expandable section below, just for words instead of characters.

<details>
<summary>Expand for example calculation of probabilities</summary>

For example, suppose that in the text we're using for generating random letters, the so-called training text, using an order-2 Markov model, the bigram `"th"` is followed 50 times by the letter `"e"`, 20 times by the letter `"a"`, and 30 times by the letter `"o"`, because the sequences `"the"`, `"tha"`, and `"tho"` occur 50, 20, and 30 times, respectively while there are no other occurrences of `"th"` in the text we're modeling. This suggests that random text that *looks similar to the training text* should most often have an `e` after `th`, and should have an `a` or `o` following with somewhat lower frequency.

Concretely, while generating random text using an order-2 Markov process suppose we generate the bigram `"th"` --- then based on this bigram we must generate the next random character using the order-2 model. The next letter will be an 'e' with a probability of 0.5 (50/100); will be an 'a' with probability 0.2 (20/100); and will be an 'o' with probability 0.3 (30/100). If 'e' is chosen, then the next bigram used to calculate random letters will be `"he"` since the last part of the old bigram is combined with the new letter to create the next bigram used in the Markov process.

Rather than using probabilities explicitly, your code will use them implicitly. You'll store 50 occurrences of `"e"`, 20 occurrences of `"a"` and 30 occurrences of `"o"` in an `ArrayList`. You'll then choose one of these at random. This will replicate the probabilities, e.g., of 0.3 for `"o"` since there will be 30 `"o"` strings in the 100-element `ArrayList`.

</details>

<details>
<summary>Historical details of this assignment (optional)</summary>

This assignment has its roots in several places: a story named _Inflexible Logic_ now found in pages 91-98 from [_Fantasia Mathematica (Google Books)_](http://books.google.com/books?id=9Xw8tMEmXncC&printsec=frontcover&pritnsec=frontcover#PPA91,M1) and reprinted from a 1940 New Yorker story called by Russell Maloney. 
The true mathematical roots are from a 1948 monolog by Claude Shannon, [A Mathematical Theory of Communication](https://docs.google.com/viewer?a=v&pid=sites&srcid=ZGVmYXVsdGRvbWFpbnxtaWNyb3JlYWRpbmcxMmZhbGx8Z3g6MThkYzkwNzcyY2U5M2U5Ng) which discusses in detail the mathematics and intuition behind this assignment. This assignment has its roots in a Nifty Assignment designed by Joe Zachary from U. Utah, assignments from Princeton designed by Kevin Wayne and others, and the work done at Duke starting with Owen Astrachan and continuing with Jeff Forbes, Salman Azhar, Branodn Fain, and the UTAs from Compsci 201.
</details>

 
## Coding Part 1: Developing the `WordGram` Class

You'll construct a `WordGram` object by passing as constructor arguments: an array, a starting index, and the size (or order) of the `WordGram.` You'll **store the strings in an array instance variable** by copying them from the array passed to the constructor.

### Getting Started
You're given an outline of `WordGram.java` with stub (unimplemented) methods and a stub constructor. Your task will be to implement these methods in `WordGram` according to the specifications detailed below. In particular, you should implement the following

- The constructor `WordGram(String[] words, int index, int size)`
- `toString()`
- `hashCode()`
- `equals(Object other)`
- `length()`
- `shiftAdd(String last)`

There is also a `wordAt()` method, but it is already completed, you do not need to edit this method.

For `hashCode`, `equals`, and `toString`, your implementations should conform to the specifications as given in the [documentation for `Object`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html). As you develop, you will test your implementation using the *JUnit* tests in `WordGramTest`. 

Before you start coding, be sure you can run the `SimpleWordGramDriver`. The output (before you have done anything to `WordGram` should be similar to what's shown below.
```
gram = null, length = 0, hash = 0
gram = null, length = 0, hash = 0
```
Note that VS Code displays duplicate lines by printing out one line with a number to the side. So if you got similar to
```
(2) gram = null, length = 0, hash = 0
```
then that matches the above output.


### Implementing `WordGram` Constructor, `toString`, and `hashCode`

The first three methods you should implement are the constructor, `.toString()`, and `.hashCode()`. Once you have completed these, you can again run program `SimpleWordGramDriver`; you should get different output - in particular the first line should now be
```
gram = Computer Science is fun, length = 4, hash = 52791914
```

You are also provided with [JUnit tests](#junit-tests) that you can use to test your implementation. Expand the following sections for details on each of these methods.

<details>
<summary>Implement the Constructor</summary>

There are three instance variables in `WordGram`:
```
private String[] myWords;
private String myToString;
private int myHash;
```

The constructor for WordGram `public WordGram(String[] source, int start, int size)`
should store `size` strings from the array `source`, starting at index `start` (of `source`) into the private `String` array instance variable `myWords` of the `WordGram` class. The array `myWords` should contain exactly `size` strings. 

For example, suppose parameter `words` is the array below, with "this" at index 0.

| | | | | | | |
| --- | --- | --- | --- | --- | --- | --- |
| "this" | "is" | "a" | "test" |"of" |"the" |"code" |
| | | | | | |

The call `new WordGram(words,3,4)` should create this array `myWords` since the starting index is the second parameter, 3, and the size is the third parameter, 4.

| | | | |
| --- | --- | --- | --- |
| "test" | "of" | "the" | "code"|
| | | | |

You do not need to change the default values assigned to the instance variables `myToString` and `myHash` in the constructor stub; these will change when you implement the methods `.toString()` and `.hashCode()`, respectively.
</details>


<details>
<summary>Implement and override toString()</summary>

The `toString()` method (on line 84) should return a printable `String` representing all the strings stored in the `WordGram` instance variable `myWords`, each separated by a single blank space (that is, `" "`).

Don't recompute this `String` each time `toString()` is called -- instead, store the String in instance variable `myToString`. For full credit your code must only call calculate `myToString` the first time `toString()` is called; it should simply return the value stored in `myToString` on subsequent calls (remember `WordGram` is immutable, so it can't change on subsequent calls). To determine whether a given call to `toString()` is the first, you can compare to the default constructor value of `myToString`.

</details>


<details>
<summary>Implement and override hashCode()</summary>

The `hashCode()` method (on line 67) should return an `int` value based on all the strings in instance variable `myWords`. See the [Java API documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#hashCode()) for the design constraints to which a `hashCode()` method should conform. 

You will implement `.equals()` later, but we will count two `WordGram` objects as equal if their `myWords` instance variables contain the same String values in the same order. In addition, note that the Java String class already has a good [`.hashCode()` method](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#hashCode()) we can leverage. Use the `.hashCode()` of the String returned by `this.toString()` to implement this method.

Don't recompute the hash value each time `.hashCode()` is called. Similar to `.toString()`, only compute it the first time `.hashCode()` is called, and store the result in the `myHash` instance variable (again noting that it cannot change later since `WordGram` objects are immutable). On subsequent calls, simply return `myHash`. Again you can check whether this is the first call by comparing to the default `myHash` value in the Constructor.
</details>


### Implementing `equals`, `length`, and `shiftAdd`

Next you should implement the remaining three methods of the `WordGram` class. Expand each section below for details.

<details>
<summary>Implement and override equals()</summary>

The `equals()` method should return `true` when the parameter passed is a `WordGram` object with _**the same strings in the same order**_ as this object. In general, the [Java API specification of `.equals()`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object) takes an `Object` type as input. The starter code provided uses the `instanceof` operator to see if the argument is indeed a `WordGram` and returns `false` if not.

If the parameter `other` is indeed a `WordGram` object then it can and should be cast to a `WordGram`, e.g., like this (you will need to add this to the starter code):
```
WordGram wg = (WordGram) other;
```

Then the strings in the array `myWords` of `wg` can be compared to this object's strings stored in `this.myWords`. Note that `WordGram` objects of different lengths cannot be equal, and your code should check this.

</details>


<details>
<summary>Implement length()</summary>

The `length()` method should return the order or size of the `WordGram` -- this is the number of words stored in the instance variable `myWords`.
</details>


<details>
<summary>Implement shiftAdd()</summary>

If this `WordGram` has k entries then the `shiftAdd()` method should create and return a _**new**_ `WordGram` object, also with k entries, whose *first* k-1 entries are the same as the *last* k-1 entries of this `WordGram`, and whose last entry is the parameter `last`. Recall that `WordGram` objects are immutable and cannot be modified once created - **this method must create a new WordGram object** and copy values correctly to return back to the user.

For example, if `WordGram w` is 
| | | |
| --- | --- | --- |
| "apple" | "pear" | "cherry" |
| | | | 

then the method call `w.shiftAdd("lemon")` should return a new `WordGram` containing {"pear", "cherry", "lemon"}. Note that this new `WordGram` will not equal w.

Note: To implement shiftAdd you'll need to create a new `WordGram` object. The code in the method will still be able to assign values to the private instance variables of that object directly since the `shiftAdd()` method is in the `WordGram` class.

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
