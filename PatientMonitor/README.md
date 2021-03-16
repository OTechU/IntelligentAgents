# README

```
/usr/lib/jvm/java-8-jdk/bin/java -jar goal-runtime-1.0-SNAPSHOT.jar patientMonitor.mas2g
```

To show a demo, type `demoN()` in the console and execute (from toolbar button, or Ctrl+R).
To reset the agent, run `reset()` in the console.

## General Overview

The agent is monitoring a patient's metrics for values outside an acceptable range, recommending actions to take remedy the situation. Since the recommendations may depend on additional information from the patient (e.g. whether the patient has additional symptoms or has taken medication), the agent is equipped to ask the patient for that information before making a recommendation.

### Display

Recommendations are shown on the chart with an arrow indicating the actual time of the recommendation. Questions from the agent are shown with an ending question mark (?), the corresponding response from the patient is shown with an arrow (->).

### Behaviour

At each time step, the agent computes which recommendations (including questions) to make, each with associated priority. It then makes the recommendation or ask the question with the highest priority. To allow the patient some time to follow the recommendation or answer a question, it keeps track of recently made recommendations and waits 10 minutes before making the same recommendation again.

## Demo1

Run `demo1()` in the console.

* The patient starts in a normal state.
* After 5 minutes, they start exercising, which raises their blood pressure.
* When their BP exceeds 120, the agent suggests they rest, lowering their activity.
* After another few minutes, the agent asks if they have taken their BP medication. The response is no and so the agent suggests they take their medication.
* Although the patients BP begins to decreases, after 20 minutes it is still too high, and being aware that the patient has taken their BP medication recently (but not too recently that it has not a chance to take effect), the agent then suggests seeking medical advice.

## Demo2

Run `demo2()` in the console.

* The patient starts in a normal state.
* After 5 minutes, the patient develops a fever and a headache.
* When their temperature exceeds 100, the agent asks if they have a headache and/or a cough.
* Based on their response (headache but no cough), the agent asks if the patient has taken medication for a headache.
* In this case the patient has not taken any medication and so the agent recommends that they take some.
* The agent also suggests rest for the patient.
* After 30 minutes, the agent asks the patient again if they still have a headache. The patient responds yes so the agent recommends rest again.
* Finally after another 30 minutes, the agents asks again and the patient responds that their headache is now gone.

## Demo3

Run `demo3()` in the console.

* The patient starts in a normal state.
* After 5 minutes, the patient develops a fever and a headache.
* When their temperature exceeds 100, the agent asks if they have a headache and/or a cough.
* Unlike demo2, this type the patient has a cough so the agent recommends seeking medical advice since this may be the symptoms of Covid-19.