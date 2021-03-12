//require org.jfree:jfreechart:1.0.19

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
import org.jfree.chart.annotations.XYTextAnnotation
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
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
respRate = new XYSeries("Respiration Rate")
o2Saturation = new XYSeries("O2 Saturation")
activity= new XYSeries("Activity")
temperature = new XYSeries("Temperature")

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

metric = { data ->
	time += 1
	data.each { metric, value ->
		perceive("metric", metric, time, value)
		series[metric].add(time, value)
	}
}

time = 0
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

temp = 85
act = 50

action("recommend") { action ->
	annotation = new XYTextAnnotation(action, time, 80)
	annotation.font = new Font("Helvetica", Font.BOLD, 15)
	xyplot.addAnnotation(annotation)
}

Thread.start {
	for (i in 0 .. 20) {
		metric([temp: temp, act: 10, bp: 30, resp: 70])
		temp = temp + 1
		sleep(2000)
	}
}
