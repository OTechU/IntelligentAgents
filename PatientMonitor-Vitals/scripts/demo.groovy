//require org.jfree:jfreechart:1.0.19

/*
*********************************************************************************
*********************************************************************************
Use-Case: Agent Monitoring Patient with Enhanced Wearable (Steady State Example)

Agent monitoring patient for any movement outside of the normal range of markers 
being watched (those below) - any deviation from the normal range invokes the agent to:

•         Alert Patient (Omni-channel)
•         Recommend Intervention Action(s)

1 o   Heart Rate:
2 o   Blood Pressure:
3 o   Respiration Rate:
4 o   Oxygen Saturation:
5 o   Movement Levels/sedentary duration:
6 o   Temperature:

•         Monitor until back in normal range

Vitals:
*******
Heart Rate: 		NR: 60-100 bpm
Blood Pressure:  	NR: less than 120/80
Respiration Rate: 	NR: 12 to 20 breaths per minute
Oxygen Saturation: 	NR: 95-100%
Temperature: 		NR: 97.8 - 99 dgs
Movement Levels/sedentary duration: NR: Time 90 minutes

*********************************************************************************
*********************************************************************************
*/


// Imports (java)
//
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.annotations.XYTextAnnotation
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.text.SimpleAttributeSet


// Set Legend Text Attributes
//
heartRate = 	new XYSeries("Heart Rate")
bloodPressure = new XYSeries("Blood Pressure")
respRate = 		new XYSeries("Respiration Rate")
o2Saturation = 	new XYSeries("O2 Saturation")
activity= 		new XYSeries("Activity")
temperature = 	new XYSeries("Temperature")


// These can be altered in the Groovy Monitor via the command:
// metric([VAR-1: VALUE-1, VAR-2: VALUE-2, VAR-N: VALUE-N])
// 

series = [
	hr: heartRate,
	bp: bloodPressure,
	resp: respRate,
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
	values.maximumItemCount = 30
	data.addSeries(values)
}


// Chart Rendering Commands and attributes
//

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
frame.size = new Dimension(900, 900)
frame.setVisible(true)


// Set the METRIC 
//
metric = { data ->
	time += 1
	data.each { metric, value ->
		perceive("metric", metric, time, value)
		series[metric].add(time, value)
	}
}


// Set Time Origins
time = 0

// Console Construction
// 
console([
	metric: metric,
	reset: {
		perceive("reset")
		time = 0
		series.each { name, values ->
			values.clear()
		}
		xyplot.clearAnnotations()
	}
])

action("alert") { message ->
	logger.info(message.toString())
}


// ********************************************************
// Set the Normal Ranges of the Vitals being Monitored
// Perturbations will be induced via the Groove Console
// ********************************************************
// Heart Rate: 			NR: 60-100 bpm
// Blood Pressure:  	NR: less than 120/80, Low < 90, High > 120
// Respiration Rate: 	NR: 12 to 20 breaths per minute
// Oxygen Saturation: 	NR: 95-100%
// Temperature: 		NR: 97.8 - 99 dgs
// Movement Levels/sedentary duration: NR: Time 60 - Move, 20 - Rest  minutes
// ********************************************************

hr = 	75
bp = 	100
resp =  15
o2 = 	97
act = 	50
temp = 	98
	
	
// This formats and controls the "Recommended Actions"
//
// The last value in the "annotation =" is the line in the chart 
// where the Recommended Action gets displayed.
//
action("recommend") { action ->
	annotation = new XYTextAnnotation(action, time, 35)
	annotation.font = new Font("Helvetica", Font.BOLD, 25)
	xyplot.addAnnotation(annotation)
}


// 
// Begin the main monitoring loop : ALL vitals in question are monitored
//

Thread.start {
	for (i in 0 .. 1500) {
		metric([temp: temp, act: act, bp: bp, resp: resp, o2: o2, hr: hr])
		// temp = temp + 1
		sleep(1500)
	}
}











