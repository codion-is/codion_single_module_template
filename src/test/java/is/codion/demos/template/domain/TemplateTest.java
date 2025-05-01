package is.codion.demos.template.domain;

import is.codion.demos.template.domain.Template.Person;
import is.codion.framework.domain.test.DomainTest;

import org.junit.jupiter.api.Test;

final class TemplateTest extends DomainTest {

	public TemplateTest() {
		super(new Template());
	}

	@Test
	void person() {
		test(Person.TYPE);
	}
}
