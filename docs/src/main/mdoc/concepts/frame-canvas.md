# Frame and Canvas

In Doodle, a *Canvas* is an area of screen that we can draw to. This could be a native window or part of a web page, or something else. A *Frame* is a description of a Canvas, which tells a particular backend how to construct the Canvas. For example, the @scaladoc[Java2D Frame](doodle.java2d.effect.Frame) allows us to specify the title of the window, the size of the window, the background color, and more. 

Frames and Canvases are naturally back-end specific, so there is no code-level abstraction that unites, say, the @scaladoc[SVG frame](doodle.svg.effect.Frame) and the @scaladoc[Java2D Frame](doodle.java2d.effect.Frame). However, conceptually they are related so where it makes sense they have the same API.

You can get quite far in Doodle without ever worrying about Frames and Canvases. When you `draw` a picture, for example, a default Frame will be created if you don't specify one. Sometimes you must explicitly specify the Frame. Animation is one example where this is required. Other times you will want more control over the output, such as to change the background color, and here creating a Frame will give you that control.
