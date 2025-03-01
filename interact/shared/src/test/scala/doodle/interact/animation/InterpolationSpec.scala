package doodle
package interact
package animation

import cats.implicits._
import doodle.interact.syntax.all._
import org.scalacheck.Prop._
import org.scalacheck._

object InterpolationSpec extends Properties("Interpolation properties") {
  property("upTo empty range produces no output") = forAllNoShrink {
    (x: Double) =>
      x.upTo(x).forSteps(10).toList ?= List()
  }

  property("upToIncluding empty range produces single output") =
    forAllNoShrink { (x: Double) =>
      x.upToIncluding(x).forSteps(10).toList ?= List(x)
    }

  property("upTo is empty when steps is zero") = forAllNoShrink(
    Gen.choose(-100.0, 100.0) :| "Start",
    Gen.posNum[Double] :| "Difference"
  ) { (start, difference) =>
    val t = start.upTo(start + difference).forSteps(0)
    t.toList ?= List.empty
  }

  property("upToIncluding is empty when steps is zero") = forAllNoShrink(
    Gen.choose(-100.0, 100.0) :| "Start",
    Gen.posNum[Double] :| "Difference"
  ) { (start, difference) =>
    val t = start.upToIncluding(start + difference).forSteps(0)
    t.toList ?= List.empty
  }

  property("upTo produces requested number of steps when range is not empty") =
    forAllNoShrink(
      Gen.choose(-100.0, 100.0) :| "Start",
      Gen.posNum[Double] :| "Difference",
      Gen.choose(1L, 100L) :| "Steps"
    ) { (start, difference, steps) =>
      val t = start.upTo(start + difference).forSteps(steps)
      t.toList.length ?= steps.toInt
    }

  property(
    "upToIncluding produces requested number of steps when range is not empty"
  ) = forAllNoShrink(
    Gen.choose(-100.0, 100.0) :| "Start",
    Gen.posNum[Double] :| "Difference",
    Gen.choose(1L, 100L) :| "Steps"
  ) { (start, difference, steps) =>
    val t = start.upToIncluding(start + difference).forSteps(steps)
    t.toList.length ?= steps.toInt
  }

  property("upTo produces expected data") = forAllNoShrink(
    Gen.choose(0, 100) :| "Start",
    Gen.choose(0, 100) :| "Difference"
  ) { (start, difference) =>
    if (difference == 0) {
      val t = start.toDouble.upTo(start.toDouble + difference).forSteps(1)
      t.toList ?= List.empty[Double]
    } else {
      val t = start.toDouble
        .upTo(start.toDouble + difference)
        .forSteps(difference.toLong)
      t.toList ?= List.tabulate(difference) { a =>
        start.toDouble + a
      }
    }
  }

  property("upToIncluding produces expected data") = forAllNoShrink(
    Gen.choose(0, 100) :| "Start",
    Gen.choose(0, 100) :| "Difference"
  ) { (start, difference) =>
    val t = start.toDouble
      .upToIncluding(start.toDouble + difference)
      .forSteps(difference.toLong + 1)
    difference match {
      case 0 => t.toList ?= List(start.toDouble)
      case _ =>
        t.toList ?= List.tabulate(difference + 1) { a =>
          start.toDouble + a
        }
    }
  }

  property("upToIncluding ends on stop") = forAllNoShrink(
    Gen.choose(-100.0, 100.0) :| "Start",
    Gen.posNum[Double] :| "Difference",
    Gen.choose(1L, 100L) :| "Steps"
  ) { (start, difference, steps) =>
    val t = start.upToIncluding(start + difference).forSteps(steps)
    t.toList.last ?= (start + difference)
  }

  property("map transforms data") = forAllNoShrink(
    Gen.choose(-100.0, 100.0) :| "Start",
    Gen.posNum[Double] :| "Difference",
    Gen.choose(1L, 100L) :| "Steps"
  ) { (start, difference, steps) =>
    val t = start.upToIncluding(start + difference).map(a => -a).forSteps(steps)
    t.toList.last ?= -(start + difference)
  }
}
