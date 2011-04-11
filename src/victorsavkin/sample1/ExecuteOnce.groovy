package victorsavkin.sample1

import java.lang.annotation.*
import org.codehaus.groovy.transform.GroovyASTTransformationClass

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(['victorsavkin.sample1.ExecuteOnceTransformation'])
@interface ExecuteOnce {
}