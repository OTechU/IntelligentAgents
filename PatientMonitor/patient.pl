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

response(Question, Answer, MinAge, MaxAge) :-
	response(Question, Answer, Time),
	now(Now), Min is Now - MaxAge, Max is Now - MinAge,
	between(Min, Max, Time).

high_resting_bp :-
	average(act, 10, Act), Act < 50,
	current(bp, BP), BP > 120.

recommend(500, 'blood pressure medication?') :-
	high_resting_bp,
	\+ response('blood pressure medication?', _, 0, 60).

recommend(500, 'seek medical advice') :-
	high_resting_bp,
	response('blood pressure medication?', true, 20, 60).

recommend(250, 'blood pressure medication') :-
	high_resting_bp,
	response('blood pressure medication?', false, 0, 60).

recommend(200, rest) :-
	current(act, Act), Act > 80,
	current(bp, BP), BP > 120.

high_temperature :-
	current(temp, Temp), Temp > 100.

recommend(400, 'headache?') :-
	high_temperature,
	\+ response('headache?', _, 0, 30).

recommend(400, 'headache?') :-
	response('headache?', true, 30, 120).

recommend(400, 'headache medication?') :-
	high_temperature,
	response('headache?', true, 0, 30),
	response('cough?', false, 0, 30),
	\+ response('headache medication?', _, 0, 120).

recommend(400, 'headache medication') :-
	high_temperature,
	response('headache?', true, 0, 30),
	response('cough?', false, 0, 30),
	response('headache medication?', false, 0, 120).

recommend(300, rest) :-
	response('headache?', true, 0, 30),
	response('headache medication?', true, 0, 120).

recommend(400, 'cough?') :-
	high_temperature,
	\+ response('cough?', _, 0, 30).

recommend(400, 'seek medical advice') :-
	high_temperature,
	response('headache?', true, 0, 30),
	response('cough?', true, 0, 30).

recommend(100, cooldown) :-
	high_temperature.

recommend(50, exercise) :-
	average(act, 10, Act), Act < 20,
	current(bp, BP), BP < 80.

