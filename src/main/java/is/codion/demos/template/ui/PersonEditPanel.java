package is.codion.demos.template.ui;

import is.codion.demos.template.domain.Template.Person;
import is.codion.swing.framework.model.SwingEntityEditModel;
import is.codion.swing.framework.ui.EntityEditPanel;

import static is.codion.swing.common.ui.layout.Layouts.gridLayout;

public final class PersonEditPanel extends EntityEditPanel {

	public PersonEditPanel(SwingEntityEditModel editModel) {
		super(editModel);
	}

	@Override
	protected void initializeUI() {
		focus().initial().set(Person.FIRST_NAME);

		createTextField(Person.FIRST_NAME);
		createTextField(Person.LAST_NAME);

		setLayout(gridLayout(2, 1));

		addInputPanel(Person.FIRST_NAME);
		addInputPanel(Person.LAST_NAME);
	}
}
