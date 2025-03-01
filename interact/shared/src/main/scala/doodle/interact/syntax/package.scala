package doodle
package interact

package object syntax {
  object all
      extends AnimationRendererSyntax
      with AnimationWriterSyntax
      with InterpolationSyntax
      with MouseClickSyntax
      with MouseMoveSyntax
      with MouseOverSyntax
      with RedrawSyntax
  object animationRenderer extends AnimationRendererSyntax
  object animationWriter extends AnimationWriterSyntax
  object interpolation extends InterpolationSyntax
  object mouseClick extends MouseClickSyntax
  object mouseMove extends MouseMoveSyntax
  object mouseOver extends MouseOverSyntax
  object redraw extends RedrawSyntax
}
