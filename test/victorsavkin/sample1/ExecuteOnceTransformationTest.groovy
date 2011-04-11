package victorsavkin.sample1

import org.junit.Test
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import spock.lang.Specification

class ExecuteOnceTransformationTest extends Specification{

	def 'should execute a method only once'() {
		when:
		def testObject = new GroovyShell().evaluate('''
			class TestClass {
				int counter = 0

				@victorsavkin.sample1.ExecuteOnce
				void executeOnceMethod(){
					counter ++
				}
			}
			new TestClass()
		''')

		then:
		testObject.counter == 0

		when:
		testObject.executeOnceMethod()

		then:
		testObject.counter == 1

		when:
		testObject.executeOnceMethod()

		then:
		testObject.counter == 1
	}

	def 'should generate a compilation error if return type is not void'() {
		when:
		new GroovyShell().evaluate('''
			class TestClass {
				@victorsavkin.sample1.ExecuteOnce
				def executeOnceMethod(){
					'ReturnValue'
				}
			}
			new TestClass()
		''')

		then:
		def e = thrown(MultipleCompilationErrorsException)
		e.errorCollector.errorCount == 1
		def firstError = e.errorCollector.errors.first()
		firstError.cause.message.startsWith('ExecuteOnce can be applied only to methods returning void') == true
	}
}
