package victorsavkin.sample2

import static org.codehaus.groovy.control.CompilePhase.*
import spock.lang.Specification
import org.codehaus.groovy.tools.ast.TransformTestHelper

class MethodCallTraceTransformationTest extends Specification {

	def 'test'(){
		when:
		def transform = new MethodCallTraceTransformation()
		def helper = new TransformTestHelper(transform, CONVERSION)
		def clazz = helper.parse '''
			class Service {
				class Repository {
					def findUserById(id){

					}
				}

				def businessOperation(){
					def repo = new Repository()
					repo.findUserById(1)
				}
			}
		'''
		def service = clazz.newInstance()
		service.businessOperation()
		Thread.sleep(500)

		then:
		1 == 1
	}
}
