# Doodle: Compositional Vector Graphics

Copyright [Noel Welsh](http://noelwelsh.com).

Doodle is a Scala library for compositional vector graphics.

Distributed under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) license.

[![Build Status](https://travis-ci.org/creativescala/doodle.svg?branch=develop)](https://travis-ci.org/creativescala/doodle)


## Using Doodle

The current release is **0.11.2** and is on the `master` branch.

To use doodle add the following to your `build.sbt`:

~~~ scala
// Doodle is currently published for Scala 2.13 and Scala 3
libraryDependencies += "org.creativescala" %% "doodle" % "0.11.2"
~~~

Alternatively you can `git clone` or download Doodle and use it directly from the SBT console. See the instructions below.


## Documentation

[Documentation](https://www.creativescala.org/doodle/) is still a work-in-progress.

[Creative Scala][creativescala] provides another source of documentation for Doodle. Creative Scala is a free introductory Scala ebook. 

[creativescala]: http://creativescala.org/

Below we have a few tips to get you started.

### Getting Started from SBT

If you downloaded Doodle, rather than adding it to an existing Scala project, you can play around with it as follows.

1.  Start SBT:

    ~~~ bash
    bash$ sbt

    > # This is the SBT prompt. Press Ctrl+D to quit to the OS.
    ~~~

2.  Start the console:

    ~~~ coffee
    > rootJVM/console

    scala> # This is the Scala prompt. Press Ctrl+D to quit to SBT.
    ~~~

3.  Use Scala commands to draw a shape in a native window:

    ~~~ scala
    scala> (Image.circle(10).fillColor(Color.red)).draw()
    ~~~

    A window should appear containing a red circle.
    
4.  You can also save your masterpieces to a file.

    ~~~ scala
    scala> (Image.circle(10).fillColor(Color.red)).write[Png]("masterpiece.png")
    ~~~
    
    Doodle currently supports writing to PNG, GIF, and JPG formats. Just alter the type parameter of `write` accordingly.


### Attention IntelliJ Users

The Scala Plugin of IntelliJ still has an open bug which cannot resolve implicits correctly. The above example might give you an Error warning in Line XX. You can ignore this warning, or fix it using `circle.draw[Algebra, Drawing, Frame, Canvas]()`. Please use any text editor and [Metals](https://scalameta.org/metals/) to get a better experience using Doodle.


## Acknowledgements

Doodle was written by Noel Welsh with contributions from [the contributors listed by Github][github-contributors].

[github-contributors]: https://github.com/creativescala/doodle/graphs/contributors


## Notes

These are notes for developers.

### Documentation

To generate the documentation run the `documentation` task in SBT *without* changing to any project. If you do the task just seems to disappear. I have no idea why. The output will be in `docs/target/docs`. Copy this to the website and upload.

If you run out of metaspace you can run just the documentation task (i.e. `sbt documentation`) and it seems to be happier.

### Publishing

To publish a release:

* update the version number on `master` in `publish.sbt`
* update the version number in `README.md`
* run `+ clean ; compile ; publishSigned`
* run `sonatypeBundleRelease`
* tag `master` with the release version
* push tags to `origin`.
