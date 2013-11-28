import org.junit.Test;

import com.pourbaix.creature.script.generator.GeneratorService;

public class testCythandria {

	@Test
	public void testMain() {
		GeneratorService generatorService = new GeneratorService();
		// generatorService.generate("src/test/resources/_CYTHAN.txt");
		generatorService.generate(new String[0]);
	}

}
