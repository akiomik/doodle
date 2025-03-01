# Changelog

## 0.11.2 24-Jul-2022

- Add "constructors" to algebras. Described in WIP documentation. This allows
  construction of `Picture` without going via syntax, and in turn without
  requiring explicit types.


## 0.11.1 2-Jun-2022

- Add `originAt` for even more layout flexibility

- Move all `interact` syntax into `all` object. Use

    `import doodle.interact.syntax.all._`
    
  instead of 
    
    `import doodle.interact.syntax._`

- Set compiler flag to generate Java 8 compatible output

- 0.11.0 was released with a mix of Java 11 and Java 8 code, so we're pretending
  it doesn't exist.


## 0.10.1 Jan 2022

- Much more flexible layout using `Landmark` and `margin`

- Syntax moves from `import doodle.syntax._` to `import doodle.syntax.all._`.
  This is consistent with Typelevel projects and avoids name collisions between
  syntax methods and the objects on which they are defined (e.g. the `text`
  object contains a method named `text`)
  
- Add missing syntax for `strokeCap` and `strokeJoin`. Contributed by Jeff
  Martin / custommonkey

- Move to fs2 and Cats Effect 3 from Monix. I'm not convinced Monix is actively
  maintained any more. Zainab helped out immensely with this one.
  
- Remove `angle` parameter from `regularPolygon` and `star`. This is not needed
  now we have rotations and their removal simplifies the API.


## 0.9.25 16-Aug-2021

- Update to Scala 3.0, Scala 2.13.6, and Scala.js 1.6. The move to Scala 3 required:
  - removing the svg and explore projects, which have dependencies that don't
    yet work on Scala 3. They will be restored in due course.

  - updating a bunch of dependencies, changing some code, etc.

- Add equilateralTriangle path

- We will never speak of 0.9.24


## 0.9.23 11-Feb-2021

- Mouse click algebra

- Reactors support mouse click


## 0.9.22

- Java2d writers correctly use frame background color.


## 0.9.21 21-Apr-2020

- Add redraw specification to Java2D `Frame`, which allows rendering animations
  in an way that captures the animation as a static image.

- A lot of work on documentation.

- Text rendering correctly uses stroke and fill where possible. Java2D doesn't
  support fill. SVG supports both.


## 0.9.20 29-Mar-2020

- Republish because I didn't completely rebuild last time and hence published
  some byte code compiled with JVM 11.


## 0.9.19 28-Mar-2020

- Build with Java 8 to avoid class file version issues that occur when compiling
  with new JVM versions.


## 0.9.18 18-Mar-2020

- Fix error in constructing open and closed paths


## 0.9.17 8-Mar-2020

- Improve modelling of fonts. Fonts can be both bold and italic

- Java2D backend actually uses given font information to render the font

- Add `scanLeft` method to `Transducer`

- Add `Base64` wrapper type to `core` which encodes image format as well
  indicating base 64 data. Update `Base64` algebra to use.

- Add `ToPicture` algebra and syntax

  The `ToPicture` algebra provides a generic way to convert some type into
  a `Picture`. The syntax adds a method `toPicture` so one can call, e.g.
  
  ```scala
  someBase64Value.toPicture[Algebra, Drawing]
  ```
  
  There are implementations for the Java2d backend for `Base64` and
  `BufferedImage`.

  This replaces some of the functionality of the `Bitmap` algebra and it is
  likely that the `Bitmap` algebra will be removed or reworked in the future.

- Implement `Text` algebra for the SVG backend on JS


## 0.9.16 27-Feb-2020

- Build and publish for Scala 2.12 and 2.13.

- add `Monoid` instance for `Picture`.

- add golden testing, to make tests more robust against rendering errors.

- `write` and `base64` methods are usable.

- add `debug` method to `Image`.

- `Vec` apply method accept cartesian or polar coordinates, inline with `Point`.

- add `withFrameRate` utility to `Observable`.

- add `Invariant` instance for `Interpolator`, and `Interpolator` instance for `Angle`.

- add `scaleLength` method to `Point`.


## 0.9.15 19-Jan-2020

- `explore` takes a `Frame`, which means it now works again.

- Some work on the `plot` library

- Algebraic animations via `Interpolation` and `Transducer` allow easier construction and composition of common animations types.


## 0.9.14 20-Dec-2019

- `Picture` has a `Monad` instance if the underlying type `F` does.

- Refactor base64 encoding support into a separate type class, and augment the syntax with an optional frame.

- Fix `Java2dWriter` use of 0 size `BufferedImage`

- Fix error in calculating bounding box if the picture uses `at` layout. This will prevent the issue where the frame is too large or small for the enclosed picture.

- Add `Debug` algebra, which draws bounding box and origin for the given picture.

- Draw bitmaps the correct way around (they were upside-down).

- Add some easing functions, the start of a library to make animations easier.

- Miscellaneous small improvements.


## 0.9.13 21-Nov-2019

- Add text rendering via the `Text` algebra. Currently only implemented for the Java2D back-end.

- Add syntax for `Size` and `Transform` algebras

- Add derived methods for some common types of shapes to the `Path` algebra. Add syntax for these.


## 0.9.12 16-Nov-2019

- Doodle has been completely rewritten in terms of tagless final algebras. This gives extensibility needed to support diffferent features on different platforms.

- 0.9.12 add the ability to load bitmap images on the java2d backend (the `read` methods on the `Bitmap` algebra.) It's likely this API will be reworked in the future. This is very basic support for this feature.

- 0.9.11 added mouse movement events to reactors.


--- Period when I wasn't recording changes ---


## 0.6.5

Fix most SVG text rendering issues. 
- The layout is passable but not great. This will be improved with time.
- Java2D does not support fills for text, to the best of my knowledge, while SVG does. This can lead to different rendering on the two platforms, and we need to think about how we will handle this.

Add some more predefined paths
- `Image.star`
- `Image.regularPolygon`

Improvements to `Random`
- `Random.int(lower,upper)` to generate an `Int` in a given range.
- `Random.discrete((atom, p), ...)` creates a discrete distribution that chooses the atom with the given probability
- Refactoring of `Random` code that avoids strange REPL interactions.

More examples


## 0.6.4

Save to SVG
- the format is `Svg`
  E.g. `myImage.save[Svg]("filename.svg")`

Save to PDF and SVG
- use `PdfAndSvg` as the format, and specify only the base name of the file.
  E.g. `myImage.save[PdfAndSvg]("filename")` will produce `filename.pdf` and `filename.svg`
  
Reduce the size of PDF files by enabling compression


## Earlier

These changes are lost in the mists of time
