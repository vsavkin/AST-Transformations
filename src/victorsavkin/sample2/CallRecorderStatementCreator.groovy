package victorsavkin.sample2

import org.codehaus.groovy.ast.builder.AstBuilder
import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.ast.MethodNode
import groovy.transform.TupleConstructor
import org.codehaus.groovy.ast.stmt.Statement

@TupleConstructor(includeFields = true)
class CallRecorderStatementCreator {

	private MethodNode method

	Statement createStatement() {
		def className = method.declaringClass.nameWithoutPackage
		createLogStatement className, method.name, getParameterNames(method)
	}

	private getParameterNames(method) {
		method.parameters.toList().name
	}

	private createLogStatement(String className, String methodName, List parameters) {
		def logCall = createLogCall(className, methodName, parameters)
		def ast = new AstBuilder().buildFromString CONVERSION, true, logCall
		ast[0].statements[0]
	}

	private createLogCall(className, methodName, parameters) {
		def res = "victorsavkin.sample2.CallRecorder.record '${className}', '${methodName}'"
		if(parameters){
			res += ", ${parameters.join(',')}"
		}
		res
	}
}
