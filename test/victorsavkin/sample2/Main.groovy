package victorsavkin.sample2

import org.codehaus.groovy.tools.ast.TransformTestHelper
import static org.codehaus.groovy.control.CompilePhase.*

class Main {
	static void main(String[] args){
		def transform = new MethodCallTraceTransformation()
		def helper = new TransformTestHelper(transform, CONVERSION)
		def clazz = helper.parse '''
			class Service {
				class Repository {
					def findUserById(id){
					}

					def findUserByIdAndName(id, name){
					}
				}

				def businessOperation(){
					def repo = new Repository()
					repo.findUserById(1)
					repo.findUserByIdAndName([1,2], 'Victor')
				}
			}
		'''
		def service = clazz.newInstance()
		service.businessOperation()
		Thread.sleep(500)

	}
}
