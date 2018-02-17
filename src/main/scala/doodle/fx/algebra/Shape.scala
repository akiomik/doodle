/*
 * Copyright 2015 noelwelsh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package doodle
package fx
package algebra

import cats.data.Kleisli
import cats.effect.IO
import doodle.core.{Color,Point}
import doodle.layout.BoundingBox
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.{Color => FxColor}

/** Higher level shape primitives */
trait Shape extends doodle.algebra.Shape[Drawing,Unit] {
  def rectangle(width: Double, height: Double): Drawing[Unit] =
    Drawing.now{ (gc, dc, tx) =>
      val strokeWidth = dc.strokeWidth.getOrElse(0.0)
      val bb = BoundingBox.centered(strokeWidth + width, strokeWidth + height)
      val w = width / 2.0
      val h = height / 2.0

      val result =
        Kleisli{ (origin: Point) =>
          val left = origin.x - w
          val top  = origin.y - h
          // println(s"origin: ${origin}, left: ${left}, top: ${top}, width: ${width}, height: ${height}")
          render(gc, dc){ gc =>
            gc.fillRect(left, top, width, height)
          }{ gc =>
            gc.strokeRect(left, top, width, height)
          }
        }

      (bb, result)
    }

  def square(width: Double): Drawing[Unit] =
    rectangle(width, width)

  def triangle(width: Double, height: Double): Drawing[Unit] =
    Drawing.now{ (gc, dc) =>
      val strokeWidth = dc.strokeWidth.getOrElse(0.0)
      val bb = BoundingBox.centered(strokeWidth + width, strokeWidth + height)

      val w = width / 2.0
      val h = height / 2.0

      val result =
        Kleisli{ (origin: Point) =>
          val xPoints = Array[Double](origin.x - w, origin.x, origin.x + w)
          val yPoints = Array[Double](origin.y + h, origin.y - h, origin.y + h)
          render(gc, dc){ gc =>
            gc.fillPolygon(xPoints, yPoints, 3)
          }{ gc =>
            gc.strokePolygon(xPoints, yPoints, 3)
          }
        }

      (bb, result)
    }

  def circle(radius: Double): Drawing[Unit] =
    Drawing.now{ (gc, dc) =>
      val strokeWidth = dc.strokeWidth.getOrElse(0.0)
      val diameter = radius * 2.0
      val bb = BoundingBox.centered(strokeWidth + diameter, strokeWidth + diameter)
      val result =
        Kleisli{ (origin: Point) =>
          render(gc, dc){ gc =>
            gc.fillOval(origin.x - radius, origin.y - radius, radius, radius)
          }{ gc =>
            gc.strokeOval(origin.x - radius, origin.y - radius, radius, radius)
          }
        }

      (bb, result)
    }

  def render(gc: GraphicsContext, dc: FxContext)(fill: GraphicsContext => Unit)(stroke: GraphicsContext => Unit): IO[Unit] =
    IO {
      setupGraphicsContext(gc, dc)
      dc.fill.foreach(_ => fill(gc))
      dc.stroke.foreach(_ => stroke(gc))
    }

  def setupGraphicsContext(gc: GraphicsContext, dc: FxContext): Unit = {
    dc.blendMode.map(bm => gc.setGlobalBlendMode(bm))
    dc.stroke.foreach{ stroke =>
      gc.setLineWidth(stroke.width)
      gc.setStroke(colorToFxColor(stroke.color))
    }
    dc.fill.foreach{ fill =>
      gc.setFill(colorToFxColor(fill.color))
    }
  }

  def colorToFxColor(c: Color): FxColor = {
    val rgba = c.toRGBA

    FxColor.rgb(rgba.red, rgba.green, rgba.blue, rgba.alpha)
  }
}
