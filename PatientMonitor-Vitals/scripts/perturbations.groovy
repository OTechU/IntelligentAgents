
// **************************************************** 
// Manual perturbations induced via the Groovy Console
//
// **************************************************** 

// ************************************
// HEART RATE: 			NR: 60-100 bpm
// hr: heartRate
// ************************************

// Heart Rate - HIGH 
metric([ hr: 130 ])

// Heart Rate - LOW
metric([ hr: 50 ])


// ******************************************
// BLOOD PRESSURE:  	NR: less than 120/80
// bp: bloodPressure
// ******************************************

// Blood Pressure - HIGH 
metric([ bp: 130 ])

// Blood Pressure - LOW
metric([ bp: 70 ])



// *****************************************************
// RESPIRATION RATE: 	NR: 12 to 20 breaths per minute
// resp: respRate
// *****************************************************

// Respiration Rate - HIGH 
metric([ resp: 30 ])

// Respiration Rate - LOW
metric([ resp: 7 ])


// *********************************
// OXYGEN SATURATION: 	NR: 95-100%
// o2: o2Saturation
// *********************************

// O2 Saturation Level - HIGH 
metric([ o2: 120 ])

// O2 Saturation Level - LOW
metric([ o2: 80 ])


// *******************************************
// TEMPERATURE: 			NR: 97.8 - 99 dgs
// temp: temperature
// *******************************************

// Temperature - HIGH
metric([ temp: 110 ])

// Temperature - LOW
metric([ temp: 85 ])


// ************************************************************
// ACTIVITY LEVELS
// act: activity 	  NR: Time > 60m - Move, Time < 20m - Rest 
// ************************************************************


// Activity Lelvel - HIGH
metric([ act: 15 ])


// Activity Lelvel - LOW
metric([ act: 90 ])


// ************************************************************
// FULL COMBINATION LEVELS (All Vitals being Monitored)
// ************************************************************


// Full Combination HIGH Metric
metric([hr: 130 , bp: 130, resp: 30, o2: 120, temp: 110, act: 180 ])


// Full Combination LOW Metric
metric([hr: 50 , bp: 70, resp: 7, o2: 80, temp: 85, act: 15 ])


// Full Combination MIXED Metric
metric([hr: 50 , bp: 70, resp: 30, o2: 80, temp: 109, act: 65 ])




// *********************************************************************
// COMPLEX DEMOs
// @Console: demo1(), reset(), demo2(), reset(), demo3()
// Use the BELOW ONLY on the PatientMonitor or PatientMonitor-Complex Demo.
// **********************************************************************


// RESET the DEMOs after each run - run to completion.
reset()



// *********
// DEMO #1
// *********

// The patient starts in a normal state.
// After 5 minutes, they start exercising, which raises their blood pressure.
// When their BP exceeds 120, the agent suggests they rest, lowering their activity.
// After another few minutes, the agent asks if they have taken their BP medication. 
// The response is no and so the agent suggests they take their medication.
// Although the patients BP begins to decreases, after 20 minutes it is still too high, 
// and being aware that the patient has taken their BP medication recently (but not too 
// recently that it has not a chance to take effect), the agent then suggests seeking 
// medical advice.


// Demo # 1 - Activity causes blood pressure to rise that then 
// becomes untrollable with BP Meds and rest. 
demo1()



// *********
// DEMO #2
// *********
// The patient starts in a normal state.
// After 5 minutes, the patient develops a fever and a headache.
// When their temperature exceeds 100, the agent asks if they have a headache and/or a cough.
// Based on their response (headache but no cough), the agent asks if the patient has taken 
/  medication for a headache. In this case the patient has not taken any medication and so the 
// agent recommends that they take some. The agent also suggests rest for the patient.
// After 30 minutes, the agent asks the patient again if they still have a headache. The patient 
// responds yes so the agent recommends rest again. Finally after another 30 minutes, the agents 
// asks again and the patient responds that their headache is now gone.

// Demo #2 - Temperature begins to rise. Headsche? Meds? Rest? OK.
demo2()


// *********
// DEMO #3
// *********

// The patient starts in a normal state.
// After 5 minutes, the patient develops a fever and a headache.
// When their temperature exceeds 100, the agent asks if they have a headache and/or a cough.
// Unlike demo2, this type the patient *has* a cough so the agent recommends seeking medical 
// advice since this may be the symptoms of Covid-19.

// Demo #3 - Temperature begins to rise. Headsche? Caugh? Seek 
// Medical Advice due to CV-19.
demo3()



// *********
// DEMO #4
// *********


// The agent is monitoring a patient's blood glucose level, advising the patient to eat, 
// exercise or rest as required.
// In this demo, there is more delay between the recommendations and the effects to simulate 
// how a patient might take time to follow advice. The agent's memory and ability to ask 
// questions prevents invalid recommedations.

// Demo #4 - A more complex example of monitoring a patients blood glucose level, advising 
// the patient to eat, exercise, or rest as required.
demo4()




















