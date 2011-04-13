package victorsavkin.sample2

import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.VariableScope

@GroovyASTTransformation(phase = CONVERSION)
class CallRecorderTransformation implements ASTTransformation{

	private specification = new CallRecorderTransformationSpecification()

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
		def outputCreator = new CallRecorderStatementCreator(method)
		def exprList = [outputCreator.createStatement(), method.code]
		method.code = new BlockStatement(exprList, new VariableScope())
	}
}
