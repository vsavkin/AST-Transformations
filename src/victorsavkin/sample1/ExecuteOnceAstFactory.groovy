package victorsavkin.sample1

import org.codehaus.groovy.ast.builder.AstBuilder
import static org.codehaus.groovy.control.CompilePhase.*
import org.codehaus.groovy.ast.stmt.Statement

class ExecuteOnceAstFactory {

	def createGuardIfStatement(fieldName) {
		def ast = new AstBuilder().buildFromString SEMANTIC_ANALYSIS, true, """
			if(! ${fieldName}){
				${fieldName} = true
			}
		"""
		ast[0].statements[0]
	}

	def generatePrivateFieldNode(fieldName) {
		def ast = new AstBuilder().buildFromString SEMANTIC_ANALYSIS, false, """
			class ${fieldName}_Class {
				private boolean ${fieldName} = false
			}
		"""
		ast[1].fields.find{it.name == fieldName}
	}
}
