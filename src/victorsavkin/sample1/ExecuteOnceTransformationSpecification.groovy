package victorsavkin.sample1

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.syntax.SyntaxException

class ExecuteOnceTransformationSpecification extends ASTTransformationSpecification<AnnotationNode, MethodNode>{

	ExecuteOnceTransformationSpecification(){
		annotationClass = ExecuteOnce
	}

	boolean isRightReturnType(astNodes){
		MethodNode annotatedMethod = astNodes[1]
		annotatedMethod.returnType?.name == "void"
	}

	SyntaxException getSyntaxExceptionForInvalidReturnType(astNodes){
		MethodNode annotatedMethod = astNodes[1]
		new SyntaxException(
				"ExecuteOnce can be applied only to methods returning void",
				annotatedMethod.lineNumber,
				annotatedMethod.columnNumber)
	}
}
