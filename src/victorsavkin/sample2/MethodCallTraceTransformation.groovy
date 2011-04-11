package victorsavkin.sample2

import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit

import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.builder.AstBuilder

@GroovyASTTransformation(phase = CONVERSION)
class MethodCallTraceTransformation implements ASTTransformation{

	private specification = new MethodCallTraceTransformationSpecification()

	void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
		if(specification.shouldSkipTransformation(sourceUnit))
			return
		
		getAllMethodsInUnit(sourceUnit).each {
			addDebugOutputStatementToMethod it
		}
		
		specification.markUnitAsProcessed sourceUnit
	}

	private getAllMethodsInUnit(sourceUnit) {
		return sourceUnit.ast.classes.methods.flatten()
	}

	private addDebugOutputStatementToMethod(method) {
		BlockStatement code = method.code
		code.statements.add 0, createDebugOutputStatement(method)
	}

	private createDebugOutputStatement(method) {
		def parameters = createParametersOutput(method)
		def className = method.declaringClass.nameWithoutPackage
		def methodCallDebugOutput = createMethodCallDebugOutput(className, method.name, parameters)
		def debugOutputWithTimestamp = "'${methodCallDebugOutput} at ' + new Date()"
		createPrintlnStatement debugOutputWithTimestamp
	}

	private createPrintlnStatement(debugOutput) {
		def ast = new AstBuilder().buildFromString CONVERSION, true, "println $debugOutput"
		ast[0].statements[0]
	}

	private createParametersOutput(method) {
		doesMethodHaveParameters(method) ? genParametersDebugOutput(method) : "''"
	}

	private doesMethodHaveParameters(method) {
		return method.parameters
	}
	
	private genParametersDebugOutput(method) {
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
}
