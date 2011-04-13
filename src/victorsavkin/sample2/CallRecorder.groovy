package victorsavkin.sample2

import java.text.SimpleDateFormat

class CallRecorder {

	static calls = []

	static synchronized clearCalls(){
		calls.clear()
	}

	static synchronized record(String className, String methodName, Object ... args){
		calls << [className: className, method: methodName, args: args*.inspect(), date: new Date()]
	}

	static printCalls(){
		def formatter = new SimpleDateFormat("hh:mm:ss.SSS")
		calls.each {
			def args = it.args*.inspect().join(',')
			def timestamp = formatter.format(it.date)
			println "${it.className}::${it.method} (${args}) at ${timestamp}"
		}
	}
}
