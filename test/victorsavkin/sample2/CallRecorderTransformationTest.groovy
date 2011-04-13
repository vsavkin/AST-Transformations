package victorsavkin.sample2

import static org.codehaus.groovy.control.CompilePhase.*
import spock.lang.Specification
import org.codehaus.groovy.tools.ast.TransformTestHelper

class CallRecorderTransformationTest extends Specification {

	def setup(){
		CallRecorder.clearCalls()
	}

	def 'should record a method call without arguments'() {
		setup:
		def transform = new CallRecorderTransformation()
		def clazz = new TransformTestHelper(transform, CONVERSION).parse '''
			class Service {
				def method(){
				}
			}
		'''

		when:
		def service = clazz.newInstance()
		service.method()

		then:
		CallRecorder.calls.size() == 1

		def recorderCall = CallRecorder.calls.first()
		recorderCall.className == 'Service'
		recorderCall.method == 'method'
		recorderCall.args.size() == 0
		recorderCall.date != null
	}

	def 'should record a method call with arguments'() {
		setup:
		def transform = new CallRecorderTransformation()
		def clazz = new TransformTestHelper(transform, CONVERSION).parse '''
			class Service {
				def method(a,b){
				}
			}
		'''

		when:
		def service = clazz.newInstance()
		service.method(INT_ARG, STR_ARG)

		then:
		def recorderCall = CallRecorder.calls.first()
		recorderCall.args.size() == 2
		recorderCall.args[0] == INT_ARG.inspect()
		recorderCall.args[1] == STR_ARG.inspect()

		where:
		INT_ARG = 1
		STR_ARG = "aaa"
	}

	def 'should record nested calls'() {
		setup:
		def transform = new CallRecorderTransformation()
		def clazz = new TransformTestHelper(transform, CONVERSION).parse '''
			class Service {
				def method2(){
				}

				def method1(){
					method2()
				}
			}
		'''

		when:
		def service = clazz.newInstance()
		service.method1()

		then:
		CallRecorder.calls.size() == 2

		def (m1,m2) = CallRecorder.calls
		m1.method == 'method1'
		m2.method == 'method2'

		where:
		INT_ARG = 1
		STR_ARG = "aaa"
	}
}
