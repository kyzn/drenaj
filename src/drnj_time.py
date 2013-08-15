# This file defines the functions for time conversion
#

number_of_days_from_1_Jan_0000_to_1_Jan_1970 = 719529;

def drnj_time2py_time(dt):
	"""
	 dt : days from 1-Jan-0000
	 returns seconds from 1 Jan  1970
	"""
	t = (dt-number_of_days_from_1_Jan_0000_to_1_Jan_1970)*24*60*60;
	return t

def py_time2drnj_time(t):
	"""
	 t : seconds from 1-Jan-1970
	 returns days from 1 Jan 0000
	"""
	dt = number_of_days_from_1_Jan_0000_to_1_Jan_1970 + t/(24*60*60);
	return dt