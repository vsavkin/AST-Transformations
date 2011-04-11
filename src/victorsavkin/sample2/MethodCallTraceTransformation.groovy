package victorsavkin.sample2

import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit

@GroovyASTTransformation(phase = CONVERSION)
class MethodCallTraceTransformation implements ASTTransformation{

	private specification = new MethodCallTraceTransformationSpecification()

	void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
		if(specification.shouldSkipTransformation(sourceUnit))
			return
		
		getAllMethodsInUnit(sourceUnit).each {
			addMethodCallTraceStatement it
		}
		
		specification.markUnitAsProcessed sourceUnit
	}

	private getAllMethodsInUnit(sourceUnit) {
		return sourceUnit.ast.classes.methods.flatten()
	}

	private addMethodCallTraceStatement(method) {
		def outputCreator = new MethodCallTraceStatementCreator(method)
		method.code.statements.add 0, outputCreator.createStatement()
	}
}
