:- dynamic
	now/1,
	metric/3,
	recommended/2,
	response/3.

current(Metric, Value) :-
	now(Now), between(0, 20, Age),
	Time is Now - Age, 
	metric(Metric, Time, Value),!.

sum(L, Sum) :- sum(L, Sum, 0).
sum([X|L], Sum, Partial) :- sum(L, Sum, Partial + X).
sum([], Sum, Sum).

average(Metric, Range, Average) :-
	now(Now), Limit is Now - Range,
	findall(Value, (metric(Metric, Time, Value), Time >= Limit), Values),
	length(Values, N), N > 0,
	sum(Values, Sum), Average is Sum / N.

recommends(Action) :-
	findall(Priority - Action, recommend(Action, Priority), Actions),
	keysort(Actions, Sorted),
	last(Sorted, _ - Action).


% *****************************************************************
% Displayed Messages based upon deviations out of the normal ranges
% *****************************************************************

% Vitals:
% *******
% Heart Rate: 		NR: 60-100 bpm
% Blood Pressure:  	NR: less than 120/80, Low < 90, High > 120
% Respiration Rate: 	NR: 12 to 20 breaths per minute
% Oxygen Saturation: 	NR: 95-100%
% Temperature: 		NR: 97.8 - 99 dgs
% Movement Levels/Sedentary duration: NR: Time > 60m - Move, Time < 20m - Rest 


%%% TEMPERATURE

% Too High
recommend(lower_temperature, 100) :-
	current(temp, Value), Value > 99.

% Too Low
recommend(raise_temperature, 100) :-
	current(temp, Value), Value < 97.


%%% ACTIVITY LEVELS

% Too High
recommend(reduce_activity, 60) :-
	current(act, Value), Value < 20.
	
% Too Low
recommend(increase_activity, 60) :-
	current(act, Value), Value > 60.



%%% HEART RATE

% Too High
recommend(lower_heart_rate, 50) :-
	current(hr, Value), Value > 100.

% Too Low
recommend(raise_heart_rate, 50) :-
	current(hr, Value), Value < 60.


%%% BLOOD PRESSURE

% Too High
recommend(lower_blood_pressure, 50) :-
	current(bp, Value), Value > 120.

% Too Low
recommend(raise_blood_pressure, 50) :-
	current(bp, Value), Value < 90.


%%% RESPIRATION RATE

% Too High
recommend(lower_respiration, 50) :-
	current(resp, Value), Value > 20.

% Too Low
recommend(raise_respiration, 50) :-
	current(resp, Value), Value < 12.
	
	
%%% OXOGEN SATURATION

% Too High
recommend(lower_O2_levels, 50) :-
	current(o2, Value), Value > 100.

% Too Low
recommend(raise_O2_levels, 50) :-
	current(o2, Value), Value < 95.	
	
	
	
	
	
	
	
	



















