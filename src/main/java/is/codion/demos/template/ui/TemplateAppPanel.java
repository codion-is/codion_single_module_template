package is.codion.demos.template.ui;

import is.codion.common.model.CancelException;
import is.codion.common.user.User;
import is.codion.demos.template.domain.Template;
import is.codion.demos.template.domain.Template.Person;
import is.codion.demos.template.model.TemplateAppModel;
import is.codion.plugin.flatlaf.intellij.themes.arc.Arc;
import is.codion.swing.common.ui.component.indicator.ValidIndicatorFactory;
import is.codion.swing.framework.model.SwingEntityModel;
import is.codion.swing.framework.ui.EntityApplicationPanel;
import is.codion.swing.framework.ui.EntityPanel;
import is.codion.swing.framework.ui.ReferentialIntegrityErrorHandling;

import java.util.List;
import java.util.Locale;

public final class TemplateAppPanel extends EntityApplicationPanel<TemplateAppModel> {

	public TemplateAppPanel(TemplateAppModel appModel) {
		super(appModel, createEntityPanels(appModel), List.of());
	}

	private static List<EntityPanel> createEntityPanels(TemplateAppModel applicationModel) {
		SwingEntityModel personModel = applicationModel.entityModels().get(Person.TYPE);

		EntityPanel personPanel = new EntityPanel(personModel, new PersonEditPanel(personModel.editModel()));

		return List.of(personPanel);
	}

	public static void main(String[] args) throws CancelException {
		Locale.setDefault(Locale.of("en", "EN"));
		ValidIndicatorFactory.FACTORY_CLASS.set("is.codion.plugin.flatlaf.indicator.FlatLafValidIndicatorFactory");
		ReferentialIntegrityErrorHandling.REFERENTIAL_INTEGRITY_ERROR_HANDLING
						.set(ReferentialIntegrityErrorHandling.DISPLAY_DEPENDENCIES);
		EntityApplicationPanel.builder(TemplateAppModel.class, TemplateAppPanel.class)
						.applicationName("Template")
						.applicationVersion(TemplateAppModel.VERSION)
						.domainType(Template.DOMAIN)
						.displayStartupDialog(false)
						.defaultLookAndFeel(Arc.class)
						.defaultUser(User.parse("scott:tiger"))
						.start();
	}
}
