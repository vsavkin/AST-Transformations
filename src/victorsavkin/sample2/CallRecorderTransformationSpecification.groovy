package victorsavkin.sample2

import org.codehaus.groovy.control.SourceUnit

class CallRecorderTransformationSpecification {
	private processed = []

	boolean isSourceUnitAlreadyProcessed(SourceUnit unit){
		unit.name in processed
	}

	boolean doesSourceUnitHaveClasses(SourceUnit unit){
		unit.ast?.classes
	}

	boolean shouldSkipTransformation(SourceUnit unit){
		isSourceUnitAlreadyProcessed(unit) || !doesSourceUnitHaveClasses(unit)
	}

	void markUnitAsProcessed(SourceUnit unit){
		processed << unit.name
	}
}
