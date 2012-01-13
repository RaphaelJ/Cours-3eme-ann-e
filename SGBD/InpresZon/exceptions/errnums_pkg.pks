CREATE OR REPLACE package errnums_pkg
is
	validation_ex exception;
	validation_const constant number := -20001;
	pragma exception_init (validation_ex, -20001);
	
	insertion_ex exception;
	insertion_const constant number := -20002;
	pragma exception_init (insertion_ex, -20002);
end errnums_pkg;
/