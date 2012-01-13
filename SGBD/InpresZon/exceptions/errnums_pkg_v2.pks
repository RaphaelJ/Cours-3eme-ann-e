CREATE OR REPLACE package errnums_pkg
is
	client_ajout_ex exception;
	client_ajout_const constant number := -20001;
	pragma exception_init (client_ajout_ex, -20001);
end errnums_pkg;
/