package victorsavkin.sample1

import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit

import org.codehaus.groovy.ast.MethodNode

import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.IfStatement

@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
class ExecuteOnceTransformation implements ASTTransformation {

	private specification = new ExecuteOnceTransformationSpecification()
	private astFactory = new ExecuteOnceAstFactory()

	void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
		if(specification.shouldSkipTransformation(astNodes))
			return

		if(!specification.isRightReturnType(astNodes)){
			sourceUnit.addError specification.getSyntaxExceptionForInvalidReturnType(astNodes)
			return
		}

		MethodNode method = astNodes[1]
		def fieldName = createFieldName(method.name)

		addGuardFieldToClass fieldName, method
		addGuardIfStatementToMethod fieldName, method
	}

	private addGuardFieldToClass(fieldName, method) {
		def field = astFactory.generatePrivateFieldNode(fieldName)
		method.declaringClass.addField field
	}

	private addGuardIfStatementToMethod(fieldName, method) {
		def guardStatement = astFactory.createGuardIfStatement(fieldName)
		addAllStatementsOfMethodIntoGuardIf(guardStatement, method)
		method.code = guardStatement
	}

	private addAllStatementsOfMethodIntoGuardIf(guardIfStatement, method) {
		BlockStatement ifBlock = guardIfStatement.ifBlock
		ifBlock.statements.addAll(method.code.statements)
	}

	private createFieldName(methodName) {
		'$_victorsavkin_samples_execute_once_guard_for_' + methodName
	}
}
