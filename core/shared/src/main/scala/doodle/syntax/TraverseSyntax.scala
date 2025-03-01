/*
 * Copyright 2015-2020 Noel Welsh
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
package syntax

import cats.Traverse
import cats.instances.unit._
import doodle.algebra.Layout
import doodle.algebra.Picture
import doodle.algebra.Shape

trait TraverseSyntax {
  implicit class TraverseOps[T[_], Alg[x[_]] <: Layout[x] with Shape[x], F[_]](
      val t: T[Picture[Alg, F, Unit]]
  ) {
    import doodle.syntax.layout._

    def allOn(implicit traverse: Traverse[T]): Picture[Alg, F, Unit] = {
      val empty = Picture { (algebra: Alg[F]) =>
        algebra.empty
      }
      traverse.foldLeft(t, empty) { (accum, img) =>
        accum.on(img)
      }
    }

    def allBeside(implicit traverse: Traverse[T]): Picture[Alg, F, Unit] = {
      val empty = Picture { (algebra: Alg[F]) =>
        algebra.empty
      }
      traverse.foldLeft(t, empty) { (accum, img) =>
        accum.beside(img)
      }
    }

    def allAbove(implicit traverse: Traverse[T]): Picture[Alg, F, Unit] = {
      val empty = Picture { (algebra: Alg[F]) =>
        algebra.empty
      }
      traverse.foldLeft(t, empty) { (accum, img) =>
        accum.above(img)
      }
    }
  }
}
