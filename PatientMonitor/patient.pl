:- dynamic
	now/1,
	metric/3,
	recommended/2.

recent(Metric, Value) :-
	now(Now), between(0, 20, Age),
	Time is Now - Age, metric(Metric, Time, Value),!.

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

recommend(cooldown, 100) :-
	recent(temp, Value), Value > 98.

recommend(exercise, 50) :-
	average(act, 10, Average), Average < 20.

