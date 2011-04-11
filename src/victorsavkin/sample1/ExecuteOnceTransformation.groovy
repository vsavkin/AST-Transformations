package victorsavkin.sample1

import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit

import org.codehaus.groovy.ast.MethodNode

import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.IfStatement

@GroovyASTTransformation(phase = INSTRUCTION_SELECTION)
class ExecuteOnceTransformation implements ASTTransformation {

	private specification = new ExecuteOnceTransformationSpecification()

	void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
		if(specification.shouldSkipTransformation(astNodes))
			return

		if(!specification.isRightReturnType(astNodes)){
			sourceUnit.addError specification.getSyntaxExceptionForInvalidReturnType(astNodes)
			return
		}

		MethodNode annotatedMethod = astNodes[1]
		def fieldName = createFieldName(annotatedMethod.name)

		addGuardFieldToClass(fieldName, annotatedMethod)
		addGuardIfStatementToMethod(fieldName, annotatedMethod)
	}


	private addGuardFieldToClass(String fieldName, MethodNode annotatedMethod) {
		def field = generatePrivateFieldNode(fieldName)
		annotatedMethod.declaringClass.addField field
	}

	private addGuardIfStatementToMethod(String fieldName, MethodNode annotatedMethod) {
		def guardStatement = createGuardIfStatement(fieldName)
		addAllStatementsOfMethodIntoGuardIf(guardStatement, annotatedMethod)
		annotatedMethod.code = guardStatement
	}

	private addAllStatementsOfMethodIntoGuardIf(IfStatement guardStatement, MethodNode methodNode) {
		BlockStatement ifBlock = guardStatement.ifBlock
		ifBlock.statements.addAll(methodNode.code.statements)
	}


	private createGuardIfStatement(fieldName) {
		def ast = new AstBuilder().buildFromString INSTRUCTION_SELECTION, true, """
			if(! ${fieldName}){
				${fieldName} = true
			}
		"""
		ast[0].statements[0]
	}

	private generatePrivateFieldNode(fieldName) {
		def ast = new AstBuilder().buildFromString INSTRUCTION_SELECTION, false, """
			class ${fieldName}_Class {
				private boolean ${fieldName} = false
			}
		"""
		ast[1].fields.find{it.name == fieldName}
	}

	private createFieldName(methodName) {
		'$_victorsavkin_samples_execute_once_guard_for_' + methodName
	}
}
