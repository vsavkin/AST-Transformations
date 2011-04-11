package victorsavkin.sample2

import org.codehaus.groovy.ast.builder.AstBuilder
import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.ast.MethodNode
import groovy.transform.TupleConstructor
import org.codehaus.groovy.ast.stmt.Statement

@TupleConstructor(includeFields = true)
class MethodCallTraceStatementCreator {

	private MethodNode method

	Statement createStatement() {
		def parameters = createParametersOutput()
		def className = method.declaringClass.nameWithoutPackage
		
		def callOutput = createMethodCallDebugOutput(className, method.name, parameters)
		def output = createDebugOutputWithTimestamp(callOutput)

		createPrintlnStatement output
	}

	private createParametersOutput() {
		doesMethodHaveParameters() ? genParametersDebugOutput() : "''"
	}

	private doesMethodHaveParameters() {
		method.parameters
	}

	private genParametersDebugOutput() {
		def parameterNames = getParameterNames(method)
		def nameValuePairs = parameterNames.collect {"'${it} = ' + ${it}.inspect()"}
		nameValuePairs.join("+ ',' + ")
	}

	private getParameterNames(method) {
		return method.parameters.toList().name
	}
		
	private createMethodCallDebugOutput(className, methodName, parameters){
		"${className}:${methodName} (' + ${parameters} + ')"
	}

	private createDebugOutputWithTimestamp(methodCallDebugOutput) {
		return "'${methodCallDebugOutput} at ' + new Date()"
	}

	private createPrintlnStatement(debugOutput) {
		def ast = new AstBuilder().buildFromString CONVERSION, true, "println $debugOutput"
		ast[0].statements[0]
	}
}
