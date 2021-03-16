//require org.jfree:jfreechart:1.5.3

/*
Use-Case: Agent Monitoring Patient with Enhanced Wearable (Steady State Example)

Agent monitoring patient for any movement outside of the normal range of markers being watched (those below) - any deviation from the normal range invokes the agent to:

•         Alert Patient (Omni-channel)
•         Recommend Intervention Action(s)
o   Heart Rate:
o   Blood Pressure:
o   Respiration Rate:
o   Oxygen Saturation:
o   Movement Levels/sedentary duration:
o   Temperature:
•         Monitor until back in normal range

Vitals:
Heart Rate: NR-  60-100 bpm
Blood Pressure:  NR less than 120/80
Respiration Rate: NR- 12 to 20 breaths per minute
Oxygen Saturation: 95-100%
Movement Levels/sedentary duration: Time???
Temperature: NR 97.8 - 99 dgs
*/

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.annotations.XYPointerAnnotation
import org.jfree.chart.annotations.XYShapeAnnotation
import org.jfree.chart.annotations.XYLineAnnotation
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.ui.TextAnchor
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.text.SimpleAttributeSet

//console()

heartRate = new XYSeries("Heart Rate")
bloodPressure = new XYSeries("Blood Pressure")
bloodGlucose = new XYSeries("Blood Glucose")
o2Saturation = new XYSeries("O2 Saturation")
activity= new XYSeries("Activity")
temperature = new XYSeries("Temperature")

series = [
	hr: heartRate,
	bp: bloodPressure,
	glu: bloodGlucose,
	o2: o2Saturation,
	act: activity,
	temp: temperature
]

data = new XYSeriesCollection()
frame = new JFrame()
renderer = new XYLineAndShapeRenderer(true, true)
chart = ChartFactory.createXYLineChart("Patient Monitor", "Time", "Value", data)
xyplot = chart.XYPlot

xyplot.setRenderer(0, renderer)
xyplot.setRenderer(1, renderer)
xyplot.setRenderer(2, renderer)
xyplot.setRenderer(3, renderer)
xyplot.setRenderer(4, renderer)
xyplot.setRenderer(5, renderer)
xyplot.setRenderer(6, renderer)

series.each { name, values ->
	values.maximumItemCount = 100
	data.addSeries(values)
}

renderer = xyplot.renderer
font = new Font("Helvetica", Font.BOLD, 20)
stroke = new BasicStroke(2.0f)
for (i in 0 .. 6) renderer.setSeriesStroke(i, stroke)
renderer.setSeriesPaint(0, Color.RED)
renderer.setSeriesPaint(1, Color.GREEN)
renderer.setSeriesPaint(2, Color.BLUE)
renderer.setSeriesPaint(3, Color.ORANGE)
renderer.setSeriesPaint(4, Color.MAGENTA)
renderer.setSeriesPaint(5, Color.CYAN)
chart.legend.itemFont = font
chart.plot.backgroundPaint = Color.WHITE
xyplot.domainGridlinePaint = Color.GRAY
xyplot.rangeGridlinePaint = Color.GRAY
frame.add(new ChartPanel(chart))
frame.size = new Dimension(600, 600)
frame.setVisible(true)


time = 0
eventTop = 40
actionHandlers = [:]
eventHandlers = [:]

annotate = { label ->
	annotation = new XYPointerAnnotation(label, time, eventTop, -1.57079632679)
	annotation.font = new Font("Helvetica", Font.BOLD, 15)
	xyplot.addAnnotation(annotation)
	eventTop -= 2
	if (eventTop < 10) {
		eventTop = 40
	}
}

now = {
	perceive("now", time)
}

metric = { data ->
	time += 1
	perceive("now", time)
	data.each { metric, value ->
		perceive("metric", metric, value)
		series[metric].add(time, value)
	}
	handlers = eventHandlers.remove(time)
	if (handlers != null) handlers.each { handler ->
		handler()
	}
}

after = { delay, handler ->
	eventHandlers.computeIfAbsent(time + delay, { new LinkedList() }).add(handler)
}

respond = { question, answer ->
	perceive("response", question, answer)
	annotate("${question} -> ${answer}")
}

reset = {
	perceive("reset")
	time = 0
	eventTop = 40
	actionHandlers = [:]
	series.each { name, values ->
		values.clear()
	}
	xyplot.clearAnnotations()
}

demo1 = {
	reset() 
	metrics = [hr: 80, bp: 90, glu: 70, o2: 98, act: 10, temp: 97]
	deltas = [:]
	medication = [:]
	
	actionHandlers["rest"] = { action ->
		metrics.act = 10
		deltas.bp = 3
	}
	
	actionHandlers["blood pressure medication"] = { action ->
		medication["blood pressure"] = true
		respond("blood pressure medication?", true)
		deltas.bp = -2
		after(10) { deltas.bp = 0 }
	}
	
	after(5) {
		metrics.act = 90 
		deltas.bp = 5
	}
	
	actionHandlers["blood pressure medication?"] = { question ->
		after(1) { respond(question, medication["blood pressure"] == true) }
	}
	
	for (i in 0 .. 60) {
		metric(metrics)
		deltas.each { metric, delta ->
			metrics[metric] += delta
		}
		sleep(500)
	}
}

demo2 = {
	reset()
	metrics = [hr: 80, bp: 90, glu: 70, o2: 98, act: 10, temp: 97]
	deltas = [:]
	medication = [:]
	headache = false
	cough = false
	
	actionHandlers["rest"] = { action ->
		metrics.act = 10
	}
	
	actionHandlers["headache medication"] = { action ->
		medication["headache"] = true
		after(1) { respond("headache medication?", true) }
		after(45) { headache = false }
	}
	
	after(5) {
		deltas.temp = 0.5
		headache = true
		after(8) { deltas.temp = 0 }
	}
	
	
	actionHandlers["headache medication?"] = { question ->
		after(1) { respond(question, medication["headache"] == true) }
	}
	
	actionHandlers["cough?"] = { question ->
		after(1) { respond(question, cough) }
	}
	
	actionHandlers["headache?"] = { question ->
		after(1) { respond(question, headache) }
	}
	
	for (i in 0 .. 90) {
		metric(metrics)
		deltas.each { metric, delta ->
			metrics[metric] += delta
		}
		if (medication["headache"]) {
			metrics.temp = (3 * metrics.temp + 97) / 4
		}
		sleep(500)
	}
}

demo3 = {
	reset()
	metrics = [hr: 80, bp: 90, glu: 70, o2: 98, act: 10, temp: 97]
	deltas = [:]
	medication = [:]
	headache = false
	cough = false
	temp_target = 97
	
	actionHandlers["rest"] = { action ->
		metrics.act = 10
	}
	
	actionHandlers["headache medication"] = { action ->
		medication["headache"] = true
		respond("headache medication?", true)
		after(15) { headache = false }
	}
	
	after(5) {
		deltas.temp = 0.5
		headache = true
		after(8) { deltas.temp = 0 }
	}
	
	after(40) {
		cough = true
		deltas.o2 = -1
		deltas.hr = 0.5
		temp_target = 103
	}
	
	actionHandlers["headache medication?"] = { question ->
		respond(question, medication["headache"] == true)
	}
	
	actionHandlers["cough?"] = { question ->
		after(1) { respond(question, cough) }
	}
	
	actionHandlers["headache?"] = { question ->
		after(1) { respond(question, headache) }
	}
	
	for (i in 0 .. 60) {
		metric(metrics)
		deltas.each { metric, delta ->
			metrics[metric] += delta
		}
		if (medication["headache"]) {
			metrics.temp = (3 * metrics.temp + temp_target) / 4
		}
		sleep(500)
	}
}

demo4 = {
	reset()
	metrics = [hr: 80, bp: 90, glu: 70, o2: 98, act: 10, temp: 97]
	deltas = [:]
	targets = [:]
	eaten = false
		
	actionHandlers["rest"] = { action ->
		targets.hr = 80
		targets.act = 10
		targets.remove("glu")
	}
	
	actionHandlers["eaten?"] = { question ->
		respond(question, eaten)
	}
	
	actionHandlers["eat"] = {
		after(5) {
			eaten = true
			targets.glu = 160
			respond("eaten?", true)
		}
		after(60) {
			eaten = false
		}
	}
	
	actionHandlers["exercise"] = {
		after(5) {
			targets.hr = 120
			targets.act = 100
			targets.remove("glu")
		}
	}
	
	for (i in 0 .. 240) {
		metric(metrics)
		deltas.each { metric, delta ->
			metrics[metric] += delta
		}
		targets.each { metric, target ->
			metrics[metric] = (3 * metrics[metric] + target) / 4
		}
		metrics.glu -= metrics.act / 20
		if (metrics.glu < 10) metrics.glu = 10
		if (metrics.glu > 160) metrics.glu = 160
		sleep(500)
	}
}

action("alert") { message ->
	logger.info(message.toString())
}

action("recommend") { action ->
	logger.info("recommended {}", action)
	annotate(action)
	handler = actionHandlers[action]
	if (handler != null) handler(action)
}

console([
	now: now,
	metric: metric,
	respond: respond,
	reset: reset,
	demo1: demo1
])



