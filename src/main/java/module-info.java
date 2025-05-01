module is.codion.demos.template {
	requires is.codion.swing.framework.ui;
	requires is.codion.plugin.flatlaf;
	requires is.codion.plugin.flatlaf.intellij.themes;

	exports is.codion.demos.template.model
					to is.codion.swing.framework.model, is.codion.swing.framework.ui;
	exports is.codion.demos.template.ui
					to is.codion.swing.framework.ui;

	provides is.codion.framework.domain.Domain
					with is.codion.demos.template.domain.Template;
}