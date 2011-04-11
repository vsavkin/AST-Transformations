package victorsavkin.sample1

class ASTTransformationSpecification<T1, T2> {

	Class annotationClass

	boolean areNotAstNodesEmpty(astNodes){
		astNodes && astNodes.size() >= 2
	}

	boolean areAstNodesOfRightTypes(astNodes){
		astNodes[0] instanceof T1 && astNodes instanceof T2
	}

	boolean isAnnotationOfRightType(astNodes){
		astNodes[0].classNode?.name == annotationClass.name
	}

	boolean shouldSkipTransformation(astNodes){
		!areNotAstNodesEmpty(astNodes) || !areAstNodesOfRightTypes(astNodes) || !isAnnotationOfRightType(astNodes)
	}
}
