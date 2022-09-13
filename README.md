# Project 2: Markov, Fall 2022

This is the directions document for Project P2 Markov in CompSci 201 at Duke University, Fall 2022. [This document details the workflow](hhttps://coursework.cs.duke.edu/cs-201-fall-22/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.

## Outline

- [Introduction](#introduction)
  - [What is a WordGram?](#what-is-a-wordgram)
  - [What is a Markov Model?](#what-is-a-markov-model)
- [Running Driver Code](#running-driver-code)
- [Coding Part 1: Developing the WordGram Class](#coding-part-1-developing-the-wordgram-class)
- [Coding Part 2: Developing the HashMarkov Class](#coding-part-2-developing-the-hashmarkov-class)
- [Analysis Questions](#analysis-questions)
- [Submitting, Reflect, Grading](#submitting-reflect-grading)


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

Markov models are random models with the Markov property. In our case, we want to create a Markov model for generating random text that looks similar to a training text. We will generate one random word at a time, and the Markov property in our context means that the probabilities for that next word will be based on the previous words.

An order-k Markov model uses order-k `WordGram`s to predict text: we sometimes call these *k-grams* where *k* refers to the order. To begin, we select a random k-gram from the *training text* (the data we use to create our model; we want to generate random text similar to the training text). Then, we look for instances of that k-gram in the training text in order to calculate the probabilities corresponding to words that might follow. We then generate a new word according to these probabilities, after which we repeat the process using the last k-1 words from the previous k-gram and the newly generated word. Continue in that fashion to create the desired number of random words. Expand below for a concrete example.

<details>
<summary>Expand for example calculation of probabilities</summary>

Suppose we are using an order 2 Markov model with the following training text (located in `testfile.txt`):

> this is a test. it is only a test. do you think it is a test?
> this test, it is ok. it is short, but it is ok to be short.

We begin with a random k-gram, suppose we get `[it, is]`. This appears 5 times in total, and is followed by `only`, `a`, `ok`, `short`, and again by `ok` each of those five times respectively. So the probability (in the training text) that `it is` is followed by `ok` is 2/5 or 40%, and for the other words is 1/5 or 20%. To generate a random word following the 2-gram `[it, is]`, we would therefore choose `ok` with 2/5 probability, or `only`, `a`, or `short` with 1/5 probability each.

Rather than calculating these probabilities explicitly, your code will use them implicitly. In particular, the `getFollows` method will return a `List` of *all* of the words that follow after a given k-gram in the training text (including duplicates), and then you will choose one of these words uniformly at random. Words that more commonly follow will be selected with higher probability by virtue of being duplicated in the `List`.

Of course, for a very small training text these probabilities may not be very meaningful, but random generative models like this can be much more powerful when supplied with large quantities of training data, in this case meaning very large training texts.

</details>

<details>
<summary>Historical details of this assignment (optional)</summary>

This assignment has its roots in several places: a story named _Inflexible Logic_ now found in pages 91-98 from [_Fantasia Mathematica (Google Books)_](http://books.google.com/books?id=9Xw8tMEmXncC&printsec=frontcover&pritnsec=frontcover#PPA91,M1) and reprinted from a 1940 New Yorker story called by Russell Maloney. 
The true mathematical roots are from a 1948 monolog by Claude Shannon, [A Mathematical Theory of Communication](https://docs.google.com/viewer?a=v&pid=sites&srcid=ZGVmYXVsdGRvbWFpbnxtaWNyb3JlYWRpbmcxMmZhbGx8Z3g6MThkYzkwNzcyY2U5M2U5Ng) which discusses in detail the mathematics and intuition behind this assignment. This assignment has its roots in a Nifty Assignment designed by Joe Zachary from U. Utah, assignments from Princeton designed by Kevin Wayne and others, and the work done at Duke starting with Owen Astrachan and continuing with Jeff Forbes, Salman Azhar, Branodn Fain, and the UTAs from Compsci 201.
</details>


## Running Driver Code

The primary driver code for this assignment is located in `MarkovDriver.java`. You should be able to run the `public static void main` method of `MarkovDriver` immediately after cloning the starter code, and should see something like the output shown below.

```
Generated 100 words with order 2 Markov Model
----------------------------------
 sorrowful ME' as to an a the with you things ring, deal enough!' ago: this unjust 
and felt Tortoise explanation; he forgetting WOULD the other it `That's person Duchess 
broken deal it. was Hare. talking simple old, thoughtfully. last Alice; while next 
away and held `poison,' the said at under King way?', only with could looking other 
it came and accidentally it she `and round smile. things children. very YOU copies 
in `why join to drowned `That had if I much did said: stop introduced or said out 
beautiful go she thank machine it at morsel beauti--FUL an 
----------------------------------
Training time = 0.012 s
Generating time = 0.007 s
```

This just looks like nonsense for now because the `WordGram` class is not correctly implemented. Inspecting `MarkovDriver` a little more closely, note:

- Some static variables used in the main method are defined at the top of class, namely:
  - `TEXT_SIZE` is the number of words to be randomly generated.
  - `RANDOM_SEED` is the random seed used to initialize the random number generator. You should always get the same random text given a particular random seed and training text.
  - `MODEL_ORDER` is the order of `WordGram`s that will be used.
  - `PRINT_MODE` can be set to true or false based on whether you want the random text generated to be printed.
- The `filename` defined at the beginning of the main method determines the file that will be used for the training text. By default it is set to `data/alice.txt`, meaning the text of *Alice in Wonderland* is being used. Note that data files are located inside the data folder.
- A `MarkovInterface` object named `generator` is created. By default, it uses `BaseMarkov` as the implementing class, a complete implementation of which is provided in the starter code. Later on, when you have developed `HashMarkov`, you can comment out the line using `BaseMarkov` and uncomment the line using `HashMarkov` to change which implementation you use.
- The `generator` then sets the specified random seed. You should get the same result on multiple runs with the same random seed. Feel free to change the seed for fun while developing and running, but *the random seed should be set to 1234 as in the default when submitting for grading*.
- The `generator` is timed in how long it takes to run two methods: first `setTraining()` and then `getRandomText()`.
- Finally, values are printed: The random text itself if `PRINT_MODE` is set to true and the time it took to train (that is, for `setTraining()` to run) the Markov model and to generate random text using the model (that is, for `getRandomText` to run). 

## Coding Part 1: Developing the `WordGram` Class

Your first task is to develop the `WordGram` class itself. You're given an outline of `WordGram.java` with appropriate instance variables declared, as well as stub (not correctly implemented methods.

Your task will be to implement these methods in `WordGram` according to the specifications provided. Javadocs in the starter code detail the expected behavior of all methods. For `hashCode`, `equals`, and `toString`, your implementations should conform to the specifications as given in the [documentation for `Object`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html). 

Note that `WordGram` objects are *immutable*, meaning they should not change after creation (similar to Java Strings). Therefore, none of the methods except the constructor should *mutate* (or change) the words assosciated with a `WordGram` object.

You can expand the sections below to see details about individual methods you need to implement.

<details>
<summary>Expand for details on the Constructor</summary>

You'll construct a `WordGram` object by passing as constructor arguments: an array, a starting index, and the size (or order) of the `WordGram.` You'll **store the strings in an array instance variable** by copying them from the array passed to the constructor.

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

You can initialize the instance variables `myToString` and `myHash` in the constructor stub to whatever default values you choose; these will change when you implement the methods `.toString()` and `.hashCode()`, respectively.
</details>

<details>
<summary>Expand for details on wordAt()</summary>

The `wordAt()` method should return the word at the given index in `myWords`. 

</details>

<details>
<summary>Expand for details on length()</summary>

The `length()` method should return the order of the `WordGram`, that is, the length of `myWords`. 
</details>

<details>
<summary>Expand for details on equals()</summary>

The `equals()` method should return `true` when the parameter passed is a `WordGram` object with **the same strings in the same order** as this object. 

The [Java API specification of `.equals()`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object) takes an `Object` type as input. Thus, the first thing the `WordGram` `equals()` method should do is check if the parameter passed is really a `WordGram` using the `instanceof` operator, and if not return false. Otherwise, the parameter can be *cast* as a `WordGram`. This is done for you in the starter code and you do not need to change it.

Then what you need to do is compare the strings in the array `myWords` of `other` and `this` (the object on which `equals()` is called). Note that `WordGram` objects of different lengths cannot be equal, and your code should check this.

</details>

<details>
<summary>Expand for details on hashCode()</summary>

The `hashCode()` method should return an `int` value based on all the strings in instance variable `myWords`. See the [Java API documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#hashCode()) for the design constraints to which a `hashCode()` method should conform. 

Note that the Java String class already has a good [`.hashCode()` method](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#hashCode()) we can leverage. Use the `.hashCode()` of the String returned by `this.toString()` to implement this method.

Since `WordGram` objects are immutable (do not change after creation), you do not need to recompute the hash value each time `.hashCode()` is called. Instead, you can compute it the first time `.hashCode()` is called (which you can check against whatever default value you might set in the constructor), and store the result in the `myHash` instance variable. On subsequent calls, simply return `myHash`.
</details>

<details>
<summary>Expand for details on shiftAdd()</summary>

If this `WordGram` has k entries then the `shiftAdd()` method should create and return a _**new**_ `WordGram` object, also with k entries, whose *first* k-1 entries are the same as the *last* k-1 entries of this `WordGram`, and whose last entry is the parameter `last`. Recall that `WordGram` objects are immutable and cannot be modified once created - **this method must create a new WordGram object** and copy values correctly to return back to the user.

For example, if `WordGram w` is 
| | | |
| --- | --- | --- |
| "apple" | "pear" | "cherry" |
| | | | 

then the method call `w.shiftAdd("lemon")` should return a new `WordGram` containing {"pear", "cherry", "lemon"}. Note that this new `WordGram` will not equal w.

Note: To implement shiftAdd you'll need to create a new `WordGram` object. The code in the method will still be able to assign values to the private instance variables of that object directly since the `shiftAdd()` method is in the `WordGram` class.

</details>

<details>
<summary>Expand for details on toString()</summary>

The `toString()` method should return a printable `String` representing all the strings stored in the `WordGram` instance variable `myWords`, each separated by a single blank space (that is, `" "`). You may find the String `join` method useful, see [the documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#join(java.lang.CharSequence,java.lang.CharSequence...)).

You do not need to recompute this `String` each time `toString()` is called -- instead, store the String in instance variable `myToString`. On subsequent calls your code should simply return the value stored in `myToString` (again using the immutability of `WordGram`s to ensure this value will not change). To determine whether a given call to `toString()` is the first, you can compare to the default constructor value of `myToString`.

</details>

After correctly implementing the `WordGram` class, re-run the `MarkovDriver`. With the default values (`TEXT_SIZE = 100`, `RANDOM_SEED = 1234`, `MODEL_ORDER = 2`, `PRINT_MODE = true`, and `filename = "data/alice.txt"`) you should see different output than when you first ran the starter code:

```
Generated 100 words with order 2 Markov Model
----------------------------------
Alice; `I daresay it's a set of verses.' `Are they in the distance, and she swam 
about, trying to touch her. `Poor little thing!' said Alice, `a great girl like you,' 
(she might well say this), `to go on with the Dutchess, it had made. `He took me 
for a few minutes to see a little worried. `Just about as it turned a corner, `Oh 
my ears and whiskers, how late it's getting!' She was close behind it was growing, 
and very neatly and simply arranged; the only one who had got its head to keep back 
the wandering hair 
----------------------------------
Training time = 0.012 s
Generating time = 0.063 s
```

Note in particular how the phrases/sentences seem better connected than what resulted from the starter code. As you will see when inspecting `BaseMarkov`, if it cannot find a given `WordGram` to calculate possible following words, it simply generates a random word from the text. Before, with an incorrect constructor, `equals()`, etc., the original starter message was just random words from *Alice in Wonderland*. Now with a correct `WordGram` class, `BaseMarkov` is generating output from the Markov model described in the intro section [What is a Markov Model?](#what-is-a-markov-model).

Caution: Seeing the output shown above does not necessarily mean that every method of your `WordGram` class is correct. In particular, `BaseMarkov` does not use hashing, and so the `hashCode()` method does not impact it, but you *will* need to correctly implement `toString()` and `hashCode()` before moving on to to the next part.

## Coding Part 2: Developing the HashMarkov Class

In this part you will develop a Markov model for generating random text using `WordGram`s and hashing. In particular, you should create a new `HashMarkov.java` file with a single public `HashMarkov` class that implements the `MarkovInterface`. 

Your implementation should have the same behavior as `BaseMarkov` in terms of implementing the interface methods and generating the same output, but it will have different performance properties due to the use of a `HashMap` in training. In particular, `HashMarkov` should create an instance variable `HashMap` that maps `WordGram`s of a given order to `List`s of words that follow that WordGram. The training text should be read (looped over) *exactly once* during the `setTraining()` method to create this map. Subsequently, the `getFollows()` method should simply return the corresponding `List` from the map, or an empty `List` if there is no entry in the map, and this should be used in `getRandomText()` to avoid having to search the training text again for every random word generated.

You can and should use `BaseMarkov` as an example for how to implement the `MarkovInterface`, noting in particular that you *must* override and implement the interface methods. The private *helper* method `getNext()` is not required, but you might consider implementing something analogous or even other private helper methods to keep your `HashMarkov` implementation organized.

### Debugging Your Code in EfficientMarkov
<details>
<summary>Click to Expand</summary>

It’s hard enough to debug code without random effects making it even harder. In the `BaseMarkov` class you’re provided, the Random object used for random-number generation is constructed as follows:

`myRandom = new Random(RANDOM_SEED);`

`RANDOM_SEED` is defined to be 1234. Using the same seed to initialize the random number stream ensures that the same random numbers are generated each time you run the program. Removing `RANDOM_SEED` and using `new Random()` will result in a different set of random numbers, and thus different text, being generated each time you run the program. This is more amusing, but harder to debug. ***If you use a seed of `RANDOM_SEED` in your `EfficientMarkov` model, you should get the same random text as when the brute-force method from `BaseMarkov` is used.*** This will help you debug your program because you can check your results with those of the code you’re given which you can rely on as being correct. You'll get this behavior "for free" since the first line of your `EfficientMarkov` constructor will be `super(order)` -- which initializes the `myRandom` instance variable.

</details>

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

## Analysis Questions

Answer the following questions in your analysis. You'll submit your analysis as a separate PDF as a separate assignment to Gradescope.

## Submitting, Reflect, Grading
You will submit the assignment on Gradescope. You can access Gradescope through the tab on Sakai.Be sure your final program is in your Git repository before you submit it for autograding on Gradescope. Please take note that changes/commits on GitLab are NOT automatically synced to Gradescope. You are welcome to submit as many times as you like, only the most recent submission will count for a grade.

Don't forget to upload a PDF for the analysis part of this assignment and mark where you answer each question. This is a separate submission in Gradescope.

After submitting, fill out the reflect form here LINK TO BE ADDED.

For this program grading will be:

TO BE ADDED