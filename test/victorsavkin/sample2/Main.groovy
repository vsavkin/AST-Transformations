package victorsavkin.sample2

import org.codehaus.groovy.tools.ast.TransformTestHelper
import static org.codehaus.groovy.control.CompilePhase.*

class Main {
	static void main(String[] args){
		def transform = new CallRecorderTransformation()
		def clazz = new TransformTestHelper(transform, CONVERSION).parse '''
			class Service {
				def factorial(n){
					(n <= 1) ? 1 : n * factorial (n - 1)
				}
			}
		'''

		def service = clazz.newInstance()
		service.factorial(5)
		CallRecorder.printCalls()
	}

}
