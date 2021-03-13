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

average(Metric, Age, Average) :-
	now(Now), Limit is Now - Age,
	findall(Value, (metric(Metric, Time, Value), Time >= Limit), Values),
	length(Values, N), N > 0,
	sum(Values, Sum), Average is Sum / N.

recommends(Action) :-
	findall(P-A, recommend(P, A), Actions),
	keysort(Actions, Sorted),
	last(Sorted, _-Action).

recommend(inf, 'blood pressure medication?') :-
	now(Now),
	average(act, 10, Act), Act < 50,
	current(bp, BP), BP > 120,
	\+ (response('blood pressure medication?', _, Time), Time + 60 >= Now).

recommend(500, emergency) :-
	now(Now),
	average(act, 10, Act), Act < 50,
	current(bp, BP), BP > 120,
	response('blood pressure medication?', true, Time),
	Min is Now - 60, Max is Now - 20,
	between(Min, Max, Time).

recommend(250, 'blood pressure medication') :-
	now(Now), 
	average(act, 10, Act), Act < 50,
	current(bp, BP), BP > 120,
	response('blood pressure medication?', false, Time),
	Time + 60 >= Now.

recommend(200, rest) :-
	current(act, Act), Act > 80,
	current(bp, BP), BP > 120.

recommend(100, cooldown) :-
	current(temp, Temp), Temp > 98.

recommend(50, exercise) :-
	average(act, 10, Act), Act < 20,
	current(bp, BP), BP < 80.

