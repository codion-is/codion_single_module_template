package is.codion.demos.template.domain;

import is.codion.framework.domain.DomainModel;
import is.codion.framework.domain.DomainType;
import is.codion.framework.domain.entity.EntityDefinition;
import is.codion.framework.domain.entity.EntityType;
import is.codion.framework.domain.entity.StringFactory;
import is.codion.framework.domain.entity.attribute.Column;

import static is.codion.framework.domain.DomainType.domainType;
import static is.codion.framework.domain.entity.KeyGenerator.identity;
import static is.codion.framework.domain.entity.OrderBy.ascending;

public final class Template extends DomainModel {

	public static final DomainType DOMAIN = domainType("Template");

	public Template() {
		super(DOMAIN);
		add(person());
	}

	public interface Person {
		EntityType TYPE = DOMAIN.entityType("template.person");

		Column<Integer> ID = TYPE.integerColumn("id");
		Column<String> FIRST_NAME = TYPE.stringColumn("first_name");
		Column<String> LAST_NAME = TYPE.stringColumn("last_name");
	}

	private EntityDefinition person() {
		return Person.TYPE.define(
										Person.ID.define()
														.primaryKey(),
										Person.FIRST_NAME.define()
														.column()
														.caption("First name")
														.searchable(true)
														.maximumLength(30)
														.nullable(false),
										Person.LAST_NAME.define()
														.column()
														.caption("Last name")
														.searchable(true)
														.maximumLength(30)
														.nullable(false))
						.keyGenerator(identity())
						.caption("Person")
						.stringFactory(StringFactory.builder()
										.value(Person.LAST_NAME)
										.text(", ")
										.value(Person.FIRST_NAME)
										.build())
						.orderBy(ascending(Person.LAST_NAME, Person.FIRST_NAME))
						.build();
	}
}
