package is.codion.demos.template.model;

import is.codion.common.version.Version;
import is.codion.demos.template.domain.Template.Person;
import is.codion.framework.db.EntityConnectionProvider;
import is.codion.swing.framework.model.SwingEntityApplicationModel;
import is.codion.swing.framework.model.SwingEntityModel;

import java.util.Collection;
import java.util.List;

public final class TemplateAppModel extends SwingEntityApplicationModel {

	public static final Version VERSION = Version.parse(TemplateAppModel.class, "/version.properties");

	public TemplateAppModel(EntityConnectionProvider connectionProvider) {
		super(connectionProvider, createEntityModels(connectionProvider));
	}

	private static Collection<SwingEntityModel> createEntityModels(EntityConnectionProvider connectionProvider) {
		return List.of(new SwingEntityModel(Person.TYPE, connectionProvider));
	}
}
