package doodle
package image

package object syntax {
  object all extends JvmImageSyntax with TraverseImageSyntax
  object image extends JvmImageSyntax
  object traverse extends TraverseImageSyntax

  /** The core object defines syntax for doodle.core, which is a convenient way
    * to avoid pulling in Algebra syntax that may conflict with Image.
    */
  object core
      extends doodle.syntax.AngleSyntax
      with doodle.syntax.NormalizedSyntax
      with doodle.syntax.UnsignedByteSyntax
}
