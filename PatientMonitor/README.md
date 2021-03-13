# README

```
/usr/lib/jvm/java-8-jdk/bin/java -jar goal-runtime-1.0-SNAPSHOT.jar patientMonitor.mas2g
```

To show a demo, type `demo1()` in the console and execute (from toolbar button, or Ctrl+R).

## Demo1

* The patient starts in a normal state.
* After 5 minutes, they start exercising, which raises their blood pressure.
* When their BP exceeds 120, the agent suggests they rest, lowering their activity.
* After another few minutes, the agent asks if they have taken their BP medication. The response is no and so the agent suggests they take their medication.
* Although the patients BP begins to decreases, after 20 minutes it is still too high, and being aware that the patient has taken their BP medication recently (but not too recently that it has not a chance to take effect), the agent then suggests a emergency intervention.

 