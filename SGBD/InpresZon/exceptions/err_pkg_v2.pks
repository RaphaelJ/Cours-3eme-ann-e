create or replace package err_pkg
is
	c_table constant pls_integer := 1; -- Default
	c_file constant pls_integer := 2;
	c_screen constant pls_integer := 4;

	procedure report_and_stop (
	   subprog in varchar2,
	   errcode in integer := sqlcode,
	   errmsg  in varchar2 := null,
	   append_sqlerrm in boolean := false,
	   strip_code in boolean := false
	);

	procedure report_and_go (
	   subprog in varchar2,
	   errcode in integer := sqlcode,
	   errmsg  in varchar2 := null,
	   append_sqlerrm in boolean := false,
	   strip_code in boolean := false
	);

	procedure logto (
	   target in pls_integer,
	   dir    in varchar2 := null,
	   file   in varchar2 := null
	);

	procedure assert (
	   condition       in boolean,
	   message         in varchar2,
	   raise_exception in boolean := true,
	   exception_name  in varchar2 := 'VALUE_ERROR'
	);
	
	function get_error_message (default_message in my_constraints.message%type)
	return my_constraints.message%TYPE;
end err_pkg;
/
